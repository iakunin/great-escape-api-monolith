package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.Subscriber;
import com.greatescape.api.monolith.web.rest.admin.SubscriberResource;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.UUIDFilter;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Criteria class for the {@link Subscriber} entity. This class is used
 * in {@link SubscriberResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /subscribers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Data
@NoArgsConstructor
public final class SubscriberCriteria implements Criteria {

    private UUIDFilter id;

    private StringFilter name;

    private StringFilter email;

    public SubscriberCriteria(SubscriberCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.email = other.email == null ? null : other.email.copy();
    }

    @Override
    public SubscriberCriteria copy() {
        return new SubscriberCriteria(this);
    }
}
