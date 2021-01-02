package com.greatescape.api.monolith.web.rest.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class FieldErrorVM {

    private final String objectName;

    private final String field;

    private final String code;
}
