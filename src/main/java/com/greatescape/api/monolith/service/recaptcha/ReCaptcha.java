package com.greatescape.api.monolith.service.recaptcha;

public interface ReCaptcha {

    void validate(String token) throws InvalidReCaptchaException;

    class InvalidReCaptchaException extends Exception {
        private static final long serialVersionUID = 1L;

        public InvalidReCaptchaException(String message) {
            super(message);
        }
    }
}
