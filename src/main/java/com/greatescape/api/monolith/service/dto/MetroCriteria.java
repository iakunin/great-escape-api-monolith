package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.Metro;
import com.greatescape.api.monolith.web.rest.MetroResource;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.UUIDFilter;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Criteria class for the {@link Metro} entity. This class is used
 * in {@link MetroResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /metros?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Data
@NoArgsConstructor
public final class MetroCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter slug;

    private StringFilter title;

    private UUIDFilter locationId;

    public MetroCriteria(MetroCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.slug = other.slug == null ? null : other.slug.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
    }

    @Override
    public MetroCriteria copy() {
        return new MetroCriteria(this);
    }
}
