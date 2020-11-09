package com.greatescape.api.monolith.web.rest.errors;

import java.io.Serializable;
import lombok.Getter;

@Getter
public final class FieldErrorVM implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String objectName;

    private final String field;

    private final String message;

    public FieldErrorVM(String dto, String field, String message) {
        this.objectName = dto;
        this.field = field;
        this.message = message;
    }
}
