package com.greatescape.api.monolith.web.rest.player;

import com.greatescape.api.monolith.config.Constants;
import com.greatescape.api.monolith.service.OtpService;
import com.greatescape.api.monolith.service.sms.Sender;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/player-api")
@RequiredArgsConstructor
@Slf4j
public class OtpResource {

    private final OtpService otpService;
    private final Sender smsSender;

    @PostMapping("/otp")
    public ResponseEntity<?> create(@Valid @RequestBody Request request) {
        log.debug("REST request to create Otp : {}", request);

        final var otp = otpService.createOtp(request.getPhone());

        try {
            smsSender.send(
                new Sender.Request(
                    request.getPhone(),
                    "Код подтверждения: " + otp.getCode()
                )
            );
        } catch (Sender.UnableToSendException e) {
            throw new RuntimeException("Unable to send sms", e);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Data
    @NoArgsConstructor
    public static class Request {
        @NotBlank @Pattern(regexp = Constants.PHONE_REGEX) private String phone;
    }
}
