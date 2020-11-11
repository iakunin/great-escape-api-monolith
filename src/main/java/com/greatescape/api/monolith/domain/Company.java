package com.greatescape.api.monolith.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

@Getter
@Setter
@Entity
@Table(name = "company")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Audited
@AuditOverride(forClass = AbstractEntity.class)
public class Company extends AbstractEntity {

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
