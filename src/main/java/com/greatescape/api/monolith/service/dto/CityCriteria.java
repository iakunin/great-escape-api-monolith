package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.City;
import com.greatescape.api.monolith.web.rest.admin.CityResource;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.UUIDFilter;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Criteria class for the {@link City} entity. This class is used
 * in {@link CityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Data
@NoArgsConstructor
public final class CityCriteria implements Criteria {

    private UUIDFilter id;

    private StringFilter slug;

    private StringFilter title;

    private StringFilter timezone;

    public CityCriteria(CityCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.slug = other.slug == null ? null : other.slug.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.timezone = other.timezone == null ? null : other.timezone.copy();
    }

    @Override
    public CityCriteria copy() {
        return new CityCriteria(this);
    }
}
