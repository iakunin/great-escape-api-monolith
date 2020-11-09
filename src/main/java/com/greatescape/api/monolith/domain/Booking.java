package com.greatescape.api.monolith.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.greatescape.api.monolith.domain.enumeration.BookingStatus;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Booking.
 */
@Getter
@Setter
@Entity
@Table(name = "booking")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Booking extends AbstractEntity {

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status;

    /**
     * @TODO: copy price from Slot, cause Slot is mutable
     */
    @NotNull
    @Column(name = "price", nullable = false)
    private Integer price;

    /**
     * @TODO: save calculated discount, cause it's mutable
     */
    @NotNull
    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "discount_in_percents", nullable = false)
    private Integer discountInPercents;

    /**
     * @TODO: save calculated commission, cause it's mutable
     */
    @NotNull
    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "commission_in_percents", nullable = false)
    private Integer commissionInPercents;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Slot slot;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "bookings", allowSetters = true)
    private Quest quest;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "bookings", allowSetters = true)
    private Player player;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Booking)) {
            return false;
        }
        return id != null && id.equals(((Booking) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Booking{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", price=" + getPrice() +
            ", discountInPercents=" + getDiscountInPercents() +
            ", commissionInPercents=" + getCommissionInPercents() +
            "}";
    }
}
