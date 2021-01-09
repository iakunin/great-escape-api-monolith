package com.greatescape.api.monolith.web.rest.errors;

import java.util.UUID;

public class SlotUnavailableForBookingException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public SlotUnavailableForBookingException(UUID id) {
        super(
            ErrorConstants.SLOT_UNAVAILABLE_FOR_BOOKING,
            String.format(
                "Slot unavailable for booking (isAvailable=false); slotId='%s'",
                id.toString()
            ),
            "booking",
            "slotUnavailableForBooking"
        );
    }
}
