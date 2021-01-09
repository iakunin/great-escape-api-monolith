package com.greatescape.api.monolith.service.impl;

import com.github.javafaker.Faker;
import com.greatescape.api.monolith.domain.Otp;
import com.greatescape.api.monolith.repository.OtpRepository;
import com.greatescape.api.monolith.service.OtpService;
import java.time.Duration;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private static final Duration EXPIRATION = Duration.ofMinutes(10);

    private final OtpRepository otpRepository;

    private final Faker faker;

    @Transactional
    @Override
    public Otp createOtp(String phone) {
        revokeAllPending(phone);

        return otpRepository.save(
            new Otp()
                .setCode(faker.numerify("###-###"))
                .setNumber(faker.random().nextInt(10, 99))
                .setPayload(phone)
                .setStatus(Otp.Status.PENDING)
                .setExpirationDate(ZonedDateTime.now().plus(EXPIRATION))
        );
    }

    private void revokeAllPending(String phone) {
        otpRepository
            .findAllByStatusAndPayload(Otp.Status.PENDING, phone)
            .forEach(otp -> {
                otp.setStatus(Otp.Status.REVOKED);
                otpRepository.save(otp);
            });
    }

    @Transactional
    @Override
    public void checkOtp(String phone, String code) throws CheckOtpException {
        Otp otp = otpRepository
            .findByStatusAndPayloadAndCode(Otp.Status.PENDING, phone, code)
            .orElseThrow(
                () -> new OtpNotFoundException(phone, code)
            );

        if (ZonedDateTime.now().isAfter(otp.getExpirationDate())) {
            otpRepository.save(otp.setStatus(Otp.Status.EXPIRED));
            throw new OtpExpiredException(otp.getExpirationDate());
        }

        otpRepository.save(otp.setStatus(Otp.Status.COMPLETED));
    }
}
