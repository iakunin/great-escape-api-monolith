package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.Slot;
import com.greatescape.api.monolith.web.rest.SlotResource;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.InstantFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;
import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link Slot} entity. This class is used
 * in {@link SlotResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /slots?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SlotCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter dateTimeLocal;

    private ZonedDateTimeFilter dateTimeWithTimeZone;

    private BooleanFilter isAvailable;

    private IntegerFilter price;

    private IntegerFilter discountInPercents;

    private IntegerFilter commissionInPercents;

    private StringFilter externalId;

    private LongFilter questId;

    public SlotCriteria() {
    }

    public SlotCriteria(SlotCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.dateTimeLocal = other.dateTimeLocal == null ? null : other.dateTimeLocal.copy();
        this.dateTimeWithTimeZone = other.dateTimeWithTimeZone == null ? null : other.dateTimeWithTimeZone.copy();
        this.isAvailable = other.isAvailable == null ? null : other.isAvailable.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.discountInPercents = other.discountInPercents == null ? null : other.discountInPercents.copy();
        this.commissionInPercents = other.commissionInPercents == null ? null : other.commissionInPercents.copy();
        this.externalId = other.externalId == null ? null : other.externalId.copy();
        this.questId = other.questId == null ? null : other.questId.copy();
    }

    @Override
    public SlotCriteria copy() {
        return new SlotCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getDateTimeLocal() {
        return dateTimeLocal;
    }

    public void setDateTimeLocal(InstantFilter dateTimeLocal) {
        this.dateTimeLocal = dateTimeLocal;
    }

    public ZonedDateTimeFilter getDateTimeWithTimeZone() {
        return dateTimeWithTimeZone;
    }

    public void setDateTimeWithTimeZone(ZonedDateTimeFilter dateTimeWithTimeZone) {
        this.dateTimeWithTimeZone = dateTimeWithTimeZone;
    }

    public BooleanFilter getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(BooleanFilter isAvailable) {
        this.isAvailable = isAvailable;
    }

    public IntegerFilter getPrice() {
        return price;
    }

    public void setPrice(IntegerFilter price) {
        this.price = price;
    }

    public IntegerFilter getDiscountInPercents() {
        return discountInPercents;
    }

    public void setDiscountInPercents(IntegerFilter discountInPercents) {
        this.discountInPercents = discountInPercents;
    }

    public IntegerFilter getCommissionInPercents() {
        return commissionInPercents;
    }

    public void setCommissionInPercents(IntegerFilter commissionInPercents) {
        this.commissionInPercents = commissionInPercents;
    }

    public StringFilter getExternalId() {
        return externalId;
    }

    public void setExternalId(StringFilter externalId) {
        this.externalId = externalId;
    }

    public LongFilter getQuestId() {
        return questId;
    }

    public void setQuestId(LongFilter questId) {
        this.questId = questId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SlotCriteria that = (SlotCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(dateTimeLocal, that.dateTimeLocal) &&
            Objects.equals(dateTimeWithTimeZone, that.dateTimeWithTimeZone) &&
            Objects.equals(isAvailable, that.isAvailable) &&
            Objects.equals(price, that.price) &&
            Objects.equals(discountInPercents, that.discountInPercents) &&
            Objects.equals(commissionInPercents, that.commissionInPercents) &&
            Objects.equals(externalId, that.externalId) &&
            Objects.equals(questId, that.questId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        dateTimeLocal,
        dateTimeWithTimeZone,
        isAvailable,
        price,
        discountInPercents,
        commissionInPercents,
        externalId,
        questId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SlotCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (dateTimeLocal != null ? "dateTimeLocal=" + dateTimeLocal + ", " : "") +
                (dateTimeWithTimeZone != null ? "dateTimeWithTimeZone=" + dateTimeWithTimeZone + ", " : "") +
                (isAvailable != null ? "isAvailable=" + isAvailable + ", " : "") +
                (price != null ? "price=" + price + ", " : "") +
                (discountInPercents != null ? "discountInPercents=" + discountInPercents + ", " : "") +
                (commissionInPercents != null ? "commissionInPercents=" + commissionInPercents + ", " : "") +
                (externalId != null ? "externalId=" + externalId + ", " : "") +
                (questId != null ? "questId=" + questId + ", " : "") +
            "}";
    }

}
