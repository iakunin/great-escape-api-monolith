package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.Thematic;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for the {@link Thematic} entity.
 */
@Getter
@Setter
public final class ThematicDTO implements Serializable {

    private Long id;

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
        if (!(o instanceof ThematicDTO)) {
            return false;
        }

        return id != null && id.equals(((ThematicDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ThematicDTO{" +
            "id=" + getId() +
            ", slug='" + getSlug() + "'" +
            ", title='" + getTitle() + "'" +
            "}";
    }
}
