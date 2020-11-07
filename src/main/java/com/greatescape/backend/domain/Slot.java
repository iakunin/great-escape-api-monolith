package com.greatescape.backend.domain;

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
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * @TODO: should be audited (with cleanup older than 6 months).\nThere could be Bookings on some Slots: just don't delete such Slots
 */
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
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateTimeLocal() {
        return dateTimeLocal;
    }

    public Slot dateTimeLocal(Instant dateTimeLocal) {
        this.dateTimeLocal = dateTimeLocal;
        return this;
    }

    public void setDateTimeLocal(Instant dateTimeLocal) {
        this.dateTimeLocal = dateTimeLocal;
    }

    public ZonedDateTime getDateTimeWithTimeZone() {
        return dateTimeWithTimeZone;
    }

    public Slot dateTimeWithTimeZone(ZonedDateTime dateTimeWithTimeZone) {
        this.dateTimeWithTimeZone = dateTimeWithTimeZone;
        return this;
    }

    public void setDateTimeWithTimeZone(ZonedDateTime dateTimeWithTimeZone) {
        this.dateTimeWithTimeZone = dateTimeWithTimeZone;
    }

    public Boolean isIsAvailable() {
        return isAvailable;
    }

    public Slot isAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
        return this;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Integer getPrice() {
        return price;
    }

    public Slot price(Integer price) {
        this.price = price;
        return this;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getDiscountInPercents() {
        return discountInPercents;
    }

    public Slot discountInPercents(Integer discountInPercents) {
        this.discountInPercents = discountInPercents;
        return this;
    }

    public void setDiscountInPercents(Integer discountInPercents) {
        this.discountInPercents = discountInPercents;
    }

    public Integer getCommissionInPercents() {
        return commissionInPercents;
    }

    public Slot commissionInPercents(Integer commissionInPercents) {
        this.commissionInPercents = commissionInPercents;
        return this;
    }

    public void setCommissionInPercents(Integer commissionInPercents) {
        this.commissionInPercents = commissionInPercents;
    }

    public String getExternalId() {
        return externalId;
    }

    public Slot externalId(String externalId) {
        this.externalId = externalId;
        return this;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getExternalState() {
        return externalState;
    }

    public Slot externalState(String externalState) {
        this.externalState = externalState;
        return this;
    }

    public void setExternalState(String externalState) {
        this.externalState = externalState;
    }

    public Quest getQuest() {
        return quest;
    }

    public Slot quest(Quest quest) {
        this.quest = quest;
        return this;
    }

    public void setQuest(Quest quest) {
        this.quest = quest;
    }
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
            ", isAvailable='" + isIsAvailable() + "'" +
            ", price=" + getPrice() +
            ", discountInPercents=" + getDiscountInPercents() +
            ", commissionInPercents=" + getCommissionInPercents() +
            ", externalId='" + getExternalId() + "'" +
            ", externalState='" + getExternalState() + "'" +
            "}";
    }
}
