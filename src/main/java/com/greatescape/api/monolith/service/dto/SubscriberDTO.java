package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.Subscriber;
import java.io.Serializable;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;

/**
 * A DTO for the {@link Subscriber} entity.
 */
@Data
public final class SubscriberDTO implements Serializable {

    private UUID id;

    @NotNull
    private String name;

    @NotNull
    @Pattern(regexp = "^\\S+@\\S+$")
    private String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriberDTO)) {
            return false;
        }

        return id != null && id.equals(((SubscriberDTO) o).id);
    }

    @Override
    public int hashCode() {
        // For more info see: https://bit.ly/37Zo2W3
        return getClass().hashCode();
    }
}
