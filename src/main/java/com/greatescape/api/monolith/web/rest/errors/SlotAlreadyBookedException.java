package com.greatescape.api.monolith.web.rest.errors;

import java.util.UUID;

public class SlotAlreadyBookedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public SlotAlreadyBookedException(UUID slotId) {
        super(
            ErrorConstants.SLOT_ALREADY_BOOKED,
            String.format("Slot already has a booking; slotId='%s'", slotId.toString()),
            "booking",
            "slotalreadybooked"
        );
    }
}
