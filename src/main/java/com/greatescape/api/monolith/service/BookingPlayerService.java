package com.greatescape.api.monolith.service;

import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Value;

public interface BookingPlayerService {

    CreateResponse create(CreateRequest request);

    @Value
    class CreateRequest {
        @NotNull UUID slotId;
        @NotNull UUID playerId;
        String comment;
    }

    @Value
    class CreateResponse {
        UUID bookingId;
        UUID slotId;
        UUID questId;
        UUID playerId;
    }
}
