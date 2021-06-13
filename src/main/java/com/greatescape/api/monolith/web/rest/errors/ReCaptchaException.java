package com.greatescape.api.monolith.web.rest.errors;

public class ReCaptchaException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public ReCaptchaException(String entityName) {
        super(
            ErrorConstants.RE_CAPTCHA,
            "reCaptcha error",
            entityName,
            "reCaptcha"
        );
    }
}
