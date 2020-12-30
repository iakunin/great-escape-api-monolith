package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.Location;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 * A DTO for the {@link Location} entity.
 */
@Data
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
        // For more info see: https://bit.ly/37Zo2W3
        return getClass().hashCode();
    }
}
