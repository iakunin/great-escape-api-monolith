package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.Metro;
import java.io.Serializable;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for the {@link Metro} entity.
 */
@Getter
@Setter
public final class MetroDTO implements Serializable {

    private UUID id;

    @NotNull
    @Size(min = 2)
    private String slug;

    @NotNull
    @Size(min = 2)
    private String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MetroDTO)) {
            return false;
        }

        return id != null && id.equals(((MetroDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "MetroDTO{" +
            "id=" + getId() +
            ", slug='" + getSlug() + "'" +
            ", title='" + getTitle() + "'" +
            "}";
    }
}
