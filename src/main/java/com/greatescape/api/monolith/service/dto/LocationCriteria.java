package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.Location;
import com.greatescape.api.monolith.web.rest.LocationResource;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.UUIDFilter;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Criteria class for the {@link Location} entity. This class is used
 * in {@link LocationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /locations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Data
@NoArgsConstructor
public class LocationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter address;

    private UUIDFilter cityId;

    private UUIDFilter metroId;

    public LocationCriteria(LocationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.cityId = other.cityId == null ? null : other.cityId.copy();
        this.metroId = other.metroId == null ? null : other.metroId.copy();
    }

    @Override
    public LocationCriteria copy() {
        return new LocationCriteria(this);
    }
}
