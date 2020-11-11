package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.Slot;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.UUID;
import javax.persistence.Lob;
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

    @Min(value = 0)
    @Max(value = 100)
    private Integer commissionInPercents;

    @NotNull
    private String externalId;

    /**
     * @TODO: change to Json.\nShould be sent to integration without changes during booking creation
     */
    @ApiModelProperty(value = "@TODO: change to Json.\nShould be sent to integration without changes during booking creation")
    @Lob
    private String externalState;

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
            ", commissionInPercents=" + getCommissionInPercents() +
            ", externalId='" + getExternalId() + "'" +
            ", externalState='" + getExternalState() + "'" +
            ", questId=" + getQuestId() +
            ", questTitle='" + getQuestTitle() + "'" +
            "}";
    }
}
