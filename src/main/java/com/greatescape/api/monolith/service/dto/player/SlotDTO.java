package com.greatescape.api.monolith.service.dto.player;

import com.greatescape.api.monolith.domain.Slot;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.UUID;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for the {@link Slot} entity.
 */
@Getter
@Setter
public final class SlotDTO implements Serializable {

    private UUID id;

    @NotNull
    private Instant dateTimeLocal;

    @NotNull
    private ZonedDateTime dateTimeWithTimeZone;

    @NotNull
    private Boolean isAvailable;

    @NotNull
    @Min(value = 0)
    private Integer price;

    @Min(value = 0)
    @Max(value = 100)
    private Integer discountInPercents;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SlotDTO)) {
            return false;
        }

        return id != null && id.equals(((SlotDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "SlotDTO{" +
            "id=" + getId() +
            ", dateTimeLocal='" + getDateTimeLocal() + "'" +
            ", dateTimeWithTimeZone='" + getDateTimeWithTimeZone() + "'" +
            ", isAvailable='" + getIsAvailable() + "'" +
            ", price=" + getPrice() +
            ", discountInPercents=" + getDiscountInPercents() +
            "}";
    }
}
