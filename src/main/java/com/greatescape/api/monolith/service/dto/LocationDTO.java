package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.Location;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for the {@link Location} entity.
 */
@Getter
@Setter
public final class LocationDTO implements Serializable {

    private UUID id;

    @NotNull
    @Size(min = 2)
    private String address;

    @Lob
    private String addressExplanation;

    private UUID cityId;

    private String cityTitle;

    private Set<MetroDTO> metros = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocationDTO)) {
            return false;
        }

        return id != null && id.equals(((LocationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "LocationDTO{" +
            "id=" + getId() +
            ", address='" + getAddress() + "'" +
            ", addressExplanation='" + getAddressExplanation() + "'" +
            ", cityId=" + getCityId() +
            ", cityTitle='" + getCityTitle() + "'" +
            ", metros='" + getMetros() + "'" +
            "}";
    }
}
