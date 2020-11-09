package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.City;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for the {@link City} entity.
 */
@Getter
@Setter
public class CityDTO implements Serializable {

    private UUID id;

    @NotNull
    @Size(min = 2)
    private String slug;

    @NotNull
    @Size(min = 2)
    private String title;

    /**
     * @TODO: store as Java TimeZone or ZoneId object
     */
    @NotNull
    @Size(min = 1)
    @ApiModelProperty(value = "@TODO: store as Java TimeZone or ZoneId object", required = true)
    private String timezone;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CityDTO)) {
            return false;
        }

        return id != null && id.equals(((CityDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CityDTO{" +
            "id=" + getId() +
            ", slug='" + getSlug() + "'" +
            ", title='" + getTitle() + "'" +
            ", timezone='" + getTimezone() + "'" +
            "}";
    }
}
