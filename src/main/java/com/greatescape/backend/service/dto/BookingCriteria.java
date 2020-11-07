package com.greatescape.backend.service.dto;

import com.greatescape.backend.domain.enumeration.BookingStatus;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link com.greatescape.backend.domain.Booking} entity. This class is used
 * in {@link com.greatescape.backend.web.rest.BookingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bookings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BookingCriteria implements Serializable, Criteria {
    /**
     * Class for filtering BookingStatus
     */
    public static class BookingStatusFilter extends Filter<BookingStatus> {

        public BookingStatusFilter() {
        }

        public BookingStatusFilter(BookingStatusFilter filter) {
            super(filter);
        }

        @Override
        public BookingStatusFilter copy() {
            return new BookingStatusFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BookingStatusFilter status;

    private IntegerFilter price;

    private IntegerFilter discountInPercents;

    private IntegerFilter commissionInPercents;

    private LongFilter slotId;

    private LongFilter questId;

    private LongFilter playerId;

    public BookingCriteria() {
    }

    public BookingCriteria(BookingCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.discountInPercents = other.discountInPercents == null ? null : other.discountInPercents.copy();
        this.commissionInPercents = other.commissionInPercents == null ? null : other.commissionInPercents.copy();
        this.slotId = other.slotId == null ? null : other.slotId.copy();
        this.questId = other.questId == null ? null : other.questId.copy();
        this.playerId = other.playerId == null ? null : other.playerId.copy();
    }

    @Override
    public BookingCriteria copy() {
        return new BookingCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BookingStatusFilter getStatus() {
        return status;
    }

    public void setStatus(BookingStatusFilter status) {
        this.status = status;
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

    public LongFilter getSlotId() {
        return slotId;
    }

    public void setSlotId(LongFilter slotId) {
        this.slotId = slotId;
    }

    public LongFilter getQuestId() {
        return questId;
    }

    public void setQuestId(LongFilter questId) {
        this.questId = questId;
    }

    public LongFilter getPlayerId() {
        return playerId;
    }

    public void setPlayerId(LongFilter playerId) {
        this.playerId = playerId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BookingCriteria that = (BookingCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(status, that.status) &&
            Objects.equals(price, that.price) &&
            Objects.equals(discountInPercents, that.discountInPercents) &&
            Objects.equals(commissionInPercents, that.commissionInPercents) &&
            Objects.equals(slotId, that.slotId) &&
            Objects.equals(questId, that.questId) &&
            Objects.equals(playerId, that.playerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        status,
        price,
        discountInPercents,
        commissionInPercents,
        slotId,
        questId,
        playerId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookingCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (price != null ? "price=" + price + ", " : "") +
                (discountInPercents != null ? "discountInPercents=" + discountInPercents + ", " : "") +
                (commissionInPercents != null ? "commissionInPercents=" + commissionInPercents + ", " : "") +
                (slotId != null ? "slotId=" + slotId + ", " : "") +
                (questId != null ? "questId=" + questId + ", " : "") +
                (playerId != null ? "playerId=" + playerId + ", " : "") +
            "}";
    }

}
