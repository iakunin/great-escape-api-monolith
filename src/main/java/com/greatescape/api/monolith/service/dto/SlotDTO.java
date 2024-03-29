package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.Slot;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * A DTO for the {@link Slot} entity.
 */
@Data
public final class SlotDTO {

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

    @Min(value = 0)
    @Max(value = 100)
    private Integer commissionInPercents;

    @NotNull
    private String externalId;

    private Map<String, Object> externalState;

    private UUID questId;

    private String questTitle;

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
        // For more info see: https://bit.ly/37Zo2W3
        return getClass().hashCode();
    }
}
