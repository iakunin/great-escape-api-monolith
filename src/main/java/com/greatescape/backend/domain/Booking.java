package com.greatescape.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.greatescape.backend.domain.enumeration.BookingStatus;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Booking.
 */
@Entity
@Table(name = "booking")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Booking implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

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
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public Booking status(BookingStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public Integer getPrice() {
        return price;
    }

    public Booking price(Integer price) {
        this.price = price;
        return this;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getDiscountInPercents() {
        return discountInPercents;
    }

    public Booking discountInPercents(Integer discountInPercents) {
        this.discountInPercents = discountInPercents;
        return this;
    }

    public void setDiscountInPercents(Integer discountInPercents) {
        this.discountInPercents = discountInPercents;
    }

    public Integer getCommissionInPercents() {
        return commissionInPercents;
    }

    public Booking commissionInPercents(Integer commissionInPercents) {
        this.commissionInPercents = commissionInPercents;
        return this;
    }

    public void setCommissionInPercents(Integer commissionInPercents) {
        this.commissionInPercents = commissionInPercents;
    }

    public Slot getSlot() {
        return slot;
    }

    public Booking slot(Slot slot) {
        this.slot = slot;
        return this;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    public Quest getQuest() {
        return quest;
    }

    public Booking quest(Quest quest) {
        this.quest = quest;
        return this;
    }

    public void setQuest(Quest quest) {
        this.quest = quest;
    }

    public Player getPlayer() {
        return player;
    }

    public Booking player(Player player) {
        this.player = player;
        return this;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
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

    // prettier-ignore
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
