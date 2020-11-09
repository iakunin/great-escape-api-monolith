package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.Subscriber;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for the {@link Subscriber} entity.
 */
@Getter
@Setter
public final class SubscriberDTO implements Serializable {

    private Long id;

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
        return 31;
    }

    @Override
    public String toString() {
        return "SubscriberDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
