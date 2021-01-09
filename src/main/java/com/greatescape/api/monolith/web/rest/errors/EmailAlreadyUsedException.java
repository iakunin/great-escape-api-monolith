package com.greatescape.api.monolith.web.rest.errors;

public class EmailAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public EmailAlreadyUsedException() {
        this("userManagement");
    }

    public EmailAlreadyUsedException(String entityName) {
        super(
            ErrorConstants.EMAIL_ALREADY_USED_TYPE,
            "Email is already in use",
            entityName,
            "emailExists"
        );
    }
}
