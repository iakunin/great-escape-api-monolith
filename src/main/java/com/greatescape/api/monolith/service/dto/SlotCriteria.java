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
import io.github.jhipster.service.filter.UUIDFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Criteria class for the {@link Slot} entity. This class is used
 * in {@link SlotResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /slots?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Data
@NoArgsConstructor
public final class SlotCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter dateTimeLocal;

    private ZonedDateTimeFilter dateTimeWithTimeZone;

    private BooleanFilter isAvailable;

    private IntegerFilter price;

    private IntegerFilter discountInPercents;

    private IntegerFilter commissionInPercents;

    private StringFilter externalId;

    private UUIDFilter questId;

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
}
