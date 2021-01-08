package com.greatescape.api.monolith.repository;

import com.greatescape.api.monolith.domain.City;
import com.greatescape.api.monolith.domain.Otp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepository extends JpaRepository<Otp, UUID>, JpaSpecificationExecutor<City> {
    List<Otp> findAllByStatusAndPayload(Otp.Status status, String payload);

    Optional<Otp> findByStatusAndPayloadAndCode(Otp.Status status, String payload, String code);
}
