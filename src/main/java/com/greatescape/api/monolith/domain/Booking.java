package com.greatescape.api.monolith.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.greatescape.api.monolith.domain.enumeration.BookingStatus;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Booking.
 */
@Entity
@Table(name = "booking")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
public class Booking extends AbstractEntity {

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status;

    @NotNull
    @Column(name = "price", nullable = false)
    private Integer price;

    @NotNull
    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "discount_in_percents", nullable = false)
    private Integer discountInPercents;

    @NotNull
    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "commission_in_percents", nullable = false)
    private Integer commissionInPercents;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "comment")
    private String comment;

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
        // For more info see: https://bit.ly/37Zo2W3
        return getClass().hashCode();
    }
}
