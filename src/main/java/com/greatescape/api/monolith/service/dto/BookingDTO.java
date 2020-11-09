package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.Booking;
import com.greatescape.api.monolith.domain.enumeration.BookingStatus;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.UUID;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for the {@link Booking} entity.
 */
@Getter
@Setter
public final class BookingDTO implements Serializable {

    private UUID id;

    @NotNull
    private BookingStatus status;

    /**
     * @TODO: copy price from Slot, cause Slot is mutable
     */
    @NotNull
    @ApiModelProperty(value = "@TODO: copy price from Slot, cause Slot is mutable", required = true)
    private Integer price;

    /**
     * @TODO: save calculated discount, cause it's mutable
     */
    @NotNull
    @Min(value = 0)
    @Max(value = 100)
    @ApiModelProperty(value = "@TODO: save calculated discount, cause it's mutable", required = true)
    private Integer discountInPercents;

    /**
     * @TODO: save calculated commission, cause it's mutable
     */
    @NotNull
    @Min(value = 0)
    @Max(value = 100)
    @ApiModelProperty(value = "@TODO: save calculated commission, cause it's mutable", required = true)
    private Integer commissionInPercents;

    private Long slotId;

    private String slotDateTimeLocal;

    private Long questId;

    private String questTitle;

    private Long playerId;

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
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookingDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", price=" + getPrice() +
            ", discountInPercents=" + getDiscountInPercents() +
            ", commissionInPercents=" + getCommissionInPercents() +
            ", slotId=" + getSlotId() +
            ", slotDateTimeLocal='" + getSlotDateTimeLocal() + "'" +
            ", questId=" + getQuestId() +
            ", questTitle='" + getQuestTitle() + "'" +
            ", playerId=" + getPlayerId() +
            ", playerPhone='" + getPlayerPhone() + "'" +
            "}";
    }
}
