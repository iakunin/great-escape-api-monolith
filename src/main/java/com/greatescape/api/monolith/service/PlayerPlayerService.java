package com.greatescape.api.monolith.service;

import com.greatescape.api.monolith.domain.Player;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Value;

/**
 * Service Interface for managing {@link Player}.
 */
public interface PlayerPlayerService {

    CreateResponse create(CreateRequest request);

    CreateResponse upsert(CreateRequest request);

    @Value
    class CreateRequest {
        @NotNull String name;
        @NotNull String phone;
        @NotNull String email;
    }

    @Value
    class CreateResponse {
        UUID playerId;
    }
}
