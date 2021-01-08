package com.greatescape.api.monolith.service;

import com.greatescape.api.monolith.domain.Otp;
import java.time.ZonedDateTime;

public interface OtpService {

    Otp createOtp(String phone);

    void checkOtp(String phone, String code) throws CheckOtpException;

    abstract class CheckOtpException extends Exception {
        private static final long serialVersionUID = 1L;

        public CheckOtpException(String message) {
            super(message);
        }
    }

    class OtpNotFoundException extends CheckOtpException {
        private static final long serialVersionUID = 1L;

        public OtpNotFoundException(String phone, String code) {
            super(
                String.format(
                    "Unable to find Otp by phone='%s' and code='%s'",
                    phone,
                    code
                )
            );
        }
    }

    class OtpExpiredException extends CheckOtpException {
        private static final long serialVersionUID = 1L;

        public OtpExpiredException(ZonedDateTime expiration) {
            super(
                String.format(
                    "Otp expired at '%s'",
                    expiration.toString()
                )
            );
        }
    }
}
