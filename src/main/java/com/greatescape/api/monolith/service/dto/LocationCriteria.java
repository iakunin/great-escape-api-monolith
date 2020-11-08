package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.Location;
import com.greatescape.api.monolith.web.rest.LocationResource;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.UUIDFilter;
import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link Location} entity. This class is used
 * in {@link LocationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /locations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LocationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter address;

    private LongFilter cityId;

    private UUIDFilter metroId;

    public LocationCriteria() {
    }

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

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getAddress() {
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public LongFilter getCityId() {
        return cityId;
    }

    public void setCityId(LongFilter cityId) {
        this.cityId = cityId;
    }

    public UUIDFilter getMetroId() {
        return metroId;
    }

    public void setMetroId(UUIDFilter metroId) {
        this.metroId = metroId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LocationCriteria that = (LocationCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(address, that.address) &&
            Objects.equals(cityId, that.cityId) &&
            Objects.equals(metroId, that.metroId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        address,
        cityId,
        metroId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (address != null ? "address=" + address + ", " : "") +
                (cityId != null ? "cityId=" + cityId + ", " : "") +
                (metroId != null ? "metroId=" + metroId + ", " : "") +
            "}";
    }

}
