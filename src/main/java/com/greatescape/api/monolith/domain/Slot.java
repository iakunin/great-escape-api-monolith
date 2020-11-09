package com.greatescape.api.monolith.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * @TODO: should be audited (with cleanup older than 6 months).\nThere could be Bookings on some Slots: just don't delete such Slots
 */
@Getter
@Setter
@Entity
@Table(name = "slot")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Slot implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "date_time_local", nullable = false)
    private Instant dateTimeLocal;

    @NotNull
    @Column(name = "date_time_with_time_zone", nullable = false)
    private ZonedDateTime dateTimeWithTimeZone;

    @NotNull
    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable;

    @NotNull
    @Min(value = 0)
    @Column(name = "price", nullable = false)
    private Integer price;

    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "discount_in_percents")
    private Integer discountInPercents;

    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "commission_in_percents")
    private Integer commissionInPercents;

    @NotNull
    @Column(name = "external_id", nullable = false)
    private String externalId;

    /**
     * @TODO: change to Json.\nShould be sent to integration without changes during booking creation
     */
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "external_state")
    private String externalState;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "slots", allowSetters = true)
    private Quest quest;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Slot)) {
            return false;
        }
        return id != null && id.equals(((Slot) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Slot{" +
            "id=" + getId() +
            ", dateTimeLocal='" + getDateTimeLocal() + "'" +
            ", dateTimeWithTimeZone='" + getDateTimeWithTimeZone() + "'" +
            ", isAvailable='" + getIsAvailable() + "'" +
            ", price=" + getPrice() +
            ", discountInPercents=" + getDiscountInPercents() +
            ", commissionInPercents=" + getCommissionInPercents() +
            ", externalId='" + getExternalId() + "'" +
            ", externalState='" + getExternalState() + "'" +
            "}";
    }
}
