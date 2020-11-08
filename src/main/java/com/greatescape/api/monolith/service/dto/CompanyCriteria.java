package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.Company;
import com.greatescape.api.monolith.web.rest.CompanyResource;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link Company} entity. This class is used
 * in {@link CompanyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /companies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CompanyCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter slug;

    private StringFilter title;

    private StringFilter legalName;

    private StringFilter taxpayerNumber;

    private IntegerFilter discountInPercents;

    private IntegerFilter commissionInPercents;

    public CompanyCriteria() {
    }

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

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getSlug() {
        return slug;
    }

    public void setSlug(StringFilter slug) {
        this.slug = slug;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getLegalName() {
        return legalName;
    }

    public void setLegalName(StringFilter legalName) {
        this.legalName = legalName;
    }

    public StringFilter getTaxpayerNumber() {
        return taxpayerNumber;
    }

    public void setTaxpayerNumber(StringFilter taxpayerNumber) {
        this.taxpayerNumber = taxpayerNumber;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CompanyCriteria that = (CompanyCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(slug, that.slug) &&
            Objects.equals(title, that.title) &&
            Objects.equals(legalName, that.legalName) &&
            Objects.equals(taxpayerNumber, that.taxpayerNumber) &&
            Objects.equals(discountInPercents, that.discountInPercents) &&
            Objects.equals(commissionInPercents, that.commissionInPercents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        slug,
        title,
        legalName,
        taxpayerNumber,
        discountInPercents,
        commissionInPercents
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompanyCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (slug != null ? "slug=" + slug + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (legalName != null ? "legalName=" + legalName + ", " : "") +
                (taxpayerNumber != null ? "taxpayerNumber=" + taxpayerNumber + ", " : "") +
                (discountInPercents != null ? "discountInPercents=" + discountInPercents + ", " : "") +
                (commissionInPercents != null ? "commissionInPercents=" + commissionInPercents + ", " : "") +
            "}";
    }

}
