package com.greatescape.api.monolith.web.rest.errors;

import lombok.Getter;

@Getter
public final class FieldErrorVM {

    private final String objectName;

    private final String field;

    private final String code;

    public FieldErrorVM(String dto, String field, String code) {
        this.objectName = dto;
        this.field = field;
        this.code = code;
    }
}
