package com.greatescape.api.monolith.web.rest.errors;

import java.util.UUID;

public class SlotTimeAlreadyPassedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public SlotTimeAlreadyPassedException(UUID id) {
        super(
            ErrorConstants.SLOT_TIME_ALREADY_PASSED,
            String.format("Slot time already passed; slotId='%s'", id.toString()),
            "booking",
            "slottimealreadypassed"
        );
    }
}
