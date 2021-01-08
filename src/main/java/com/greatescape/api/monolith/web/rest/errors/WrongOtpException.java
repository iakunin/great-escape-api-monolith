package com.greatescape.api.monolith.web.rest.errors;

public class WrongOtpException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public WrongOtpException() {
        super(
            ErrorConstants.WRONG_OTP,
            "Wrong OTP",
            "booking",
            "wrongotp"
        );
    }
}
