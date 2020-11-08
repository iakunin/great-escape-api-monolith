package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.Slot;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import javax.persistence.Lob;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link Slot} entity.
 */
@ApiModel(description = "@TODO: should be audited (with cleanup older than 6 months).\nThere could be Bookings on some Slots: just don't delete such Slots")
public class SlotDTO implements Serializable {

    private Long id;

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


    private Long questId;

    private String questTitle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateTimeLocal() {
        return dateTimeLocal;
    }

    public void setDateTimeLocal(Instant dateTimeLocal) {
        this.dateTimeLocal = dateTimeLocal;
    }

    public ZonedDateTime getDateTimeWithTimeZone() {
        return dateTimeWithTimeZone;
    }

    public void setDateTimeWithTimeZone(ZonedDateTime dateTimeWithTimeZone) {
        this.dateTimeWithTimeZone = dateTimeWithTimeZone;
    }

    public Boolean isIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getDiscountInPercents() {
        return discountInPercents;
    }

    public void setDiscountInPercents(Integer discountInPercents) {
        this.discountInPercents = discountInPercents;
    }

    public Integer getCommissionInPercents() {
        return commissionInPercents;
    }

    public void setCommissionInPercents(Integer commissionInPercents) {
        this.commissionInPercents = commissionInPercents;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getExternalState() {
        return externalState;
    }

    public void setExternalState(String externalState) {
        this.externalState = externalState;
    }

    public Long getQuestId() {
        return questId;
    }

    public void setQuestId(Long questId) {
        this.questId = questId;
    }

    public String getQuestTitle() {
        return questTitle;
    }

    public void setQuestTitle(String questTitle) {
        this.questTitle = questTitle;
    }

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

    // prettier-ignore
    @Override
    public String toString() {
        return "SlotDTO{" +
            "id=" + getId() +
            ", dateTimeLocal='" + getDateTimeLocal() + "'" +
            ", dateTimeWithTimeZone='" + getDateTimeWithTimeZone() + "'" +
            ", isAvailable='" + isIsAvailable() + "'" +
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
