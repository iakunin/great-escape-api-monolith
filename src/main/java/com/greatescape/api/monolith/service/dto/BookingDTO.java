package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.Booking;
import com.greatescape.api.monolith.domain.enumeration.BookingStatus;
import java.util.UUID;
import javax.persistence.Lob;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * A DTO for the {@link Booking} entity.
 */
@Data
public final class BookingDTO {

    private UUID id;

    @NotNull
    private BookingStatus status;

    @NotNull
    private Integer price;

    @NotNull
    @Min(value = 0)
    @Max(value = 100)
    private Integer discountInPercents;

    @NotNull
    @Min(value = 0)
    @Max(value = 100)
    private Integer commissionInPercents;

    @Lob
    private String comment;

    private UUID slotId;

    private String slotDateTimeLocal;

    private UUID questId;

    private String questTitle;

    private UUID playerId;

    private String playerPhone;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookingDTO)) {
            return false;
        }

        return id != null && id.equals(((BookingDTO) o).id);
    }

    @Override
    public int hashCode() {
        // For more info see: https://bit.ly/37Zo2W3
        return getClass().hashCode();
    }
}
