package com.greatescape.api.monolith.web.rest.errors;

import java.util.UUID;

public class PlayerNotFoundException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public PlayerNotFoundException(UUID id) {
        super(
            ErrorConstants.PLAYER_NOT_FOUND,
            String.format("Player is not found by id='%s'", id.toString()),
            "booking",
            "playerNotFound"
        );
    }
}
