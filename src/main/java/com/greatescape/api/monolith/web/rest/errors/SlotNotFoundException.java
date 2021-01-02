package com.greatescape.api.monolith.web.rest.errors;

import java.util.UUID;

public class SlotNotFoundException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public SlotNotFoundException(UUID id) {
        super(
            ErrorConstants.SLOT_NOT_FOUND,
            String.format("Slot is not found by id='%s'", id.toString()),
            "booking",
            "slotnotfound"
        );
    }
}
