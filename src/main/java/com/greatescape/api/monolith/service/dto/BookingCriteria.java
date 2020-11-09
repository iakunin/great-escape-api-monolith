package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.Booking;
import com.greatescape.api.monolith.domain.enumeration.BookingStatus;
import com.greatescape.api.monolith.web.rest.BookingResource;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.UUIDFilter;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Criteria class for the {@link Booking} entity. This class is used
 * in {@link BookingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bookings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Data
@NoArgsConstructor
public class BookingCriteria implements Serializable, Criteria {
    /**
     * Class for filtering BookingStatus
     */
    @NoArgsConstructor
    public static class BookingStatusFilter extends Filter<BookingStatus> {

        public BookingStatusFilter(BookingStatusFilter filter) {
            super(filter);
        }

        @Override
        public BookingStatusFilter copy() {
            return new BookingStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private BookingStatusFilter status;

    private IntegerFilter price;

    private IntegerFilter discountInPercents;

    private IntegerFilter commissionInPercents;

    private LongFilter slotId;

    private LongFilter questId;

    private LongFilter playerId;

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
}
