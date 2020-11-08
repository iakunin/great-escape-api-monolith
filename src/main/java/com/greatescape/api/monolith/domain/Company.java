package com.greatescape.api.monolith.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @TODO: should be audited
 */
@Entity
@Table(name = "company")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 2)
    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @NotNull
    @Size(min = 2)
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Size(min = 2)
    @Column(name = "legal_name", nullable = false)
    private String legalName;

    @Pattern(regexp = "^\\d+$")
    @Column(name = "taxpayer_number")
    private String taxpayerNumber;

    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "discount_in_percents")
    private Integer discountInPercents;

    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "commission_in_percents")
    private Integer commissionInPercents;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public Company slug(String slug) {
        this.slug = slug;
        return this;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public Company title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLegalName() {
        return legalName;
    }

    public Company legalName(String legalName) {
        this.legalName = legalName;
        return this;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getTaxpayerNumber() {
        return taxpayerNumber;
    }

    public Company taxpayerNumber(String taxpayerNumber) {
        this.taxpayerNumber = taxpayerNumber;
        return this;
    }

    public void setTaxpayerNumber(String taxpayerNumber) {
        this.taxpayerNumber = taxpayerNumber;
    }

    public Integer getDiscountInPercents() {
        return discountInPercents;
    }

    public Company discountInPercents(Integer discountInPercents) {
        this.discountInPercents = discountInPercents;
        return this;
    }

    public void setDiscountInPercents(Integer discountInPercents) {
        this.discountInPercents = discountInPercents;
    }

    public Integer getCommissionInPercents() {
        return commissionInPercents;
    }

    public Company commissionInPercents(Integer commissionInPercents) {
        this.commissionInPercents = commissionInPercents;
        return this;
    }

    public void setCommissionInPercents(Integer commissionInPercents) {
        this.commissionInPercents = commissionInPercents;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Company)) {
            return false;
        }
        return id != null && id.equals(((Company) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Company{" +
            "id=" + getId() +
            ", slug='" + getSlug() + "'" +
            ", title='" + getTitle() + "'" +
            ", legalName='" + getLegalName() + "'" +
            ", taxpayerNumber='" + getTaxpayerNumber() + "'" +
            ", discountInPercents=" + getDiscountInPercents() +
            ", commissionInPercents=" + getCommissionInPercents() +
            "}";
    }
}
