package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.Company;
import com.greatescape.api.monolith.web.rest.CompanyResource;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Criteria class for the {@link Company} entity. This class is used
 * in {@link CompanyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /companies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Data
@NoArgsConstructor
public final class CompanyCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter slug;

    private StringFilter title;

    private StringFilter legalName;

    private StringFilter taxpayerNumber;

    private IntegerFilter discountInPercents;

    private IntegerFilter commissionInPercents;

    public CompanyCriteria(CompanyCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.slug = other.slug == null ? null : other.slug.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.legalName = other.legalName == null ? null : other.legalName.copy();
        this.taxpayerNumber = other.taxpayerNumber == null ? null : other.taxpayerNumber.copy();
        this.discountInPercents = other.discountInPercents == null ? null : other.discountInPercents.copy();
        this.commissionInPercents = other.commissionInPercents == null ? null : other.commissionInPercents.copy();
    }

    @Override
    public CompanyCriteria copy() {
        return new CompanyCriteria(this);
    }
}
