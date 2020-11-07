package com.greatescape.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Location.
 */
@Entity
@Table(name = "location")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 2)
    @Column(name = "address", nullable = false)
    private String address;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "address_explanation")
    private String addressExplanation;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "locations", allowSetters = true)
    private City city;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "location_metro",
               joinColumns = @JoinColumn(name = "location_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "metro_id", referencedColumnName = "id"))
    private Set<Metro> metros = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public Location address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressExplanation() {
        return addressExplanation;
    }

    public Location addressExplanation(String addressExplanation) {
        this.addressExplanation = addressExplanation;
        return this;
    }

    public void setAddressExplanation(String addressExplanation) {
        this.addressExplanation = addressExplanation;
    }

    public City getCity() {
        return city;
    }

    public Location city(City city) {
        this.city = city;
        return this;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Set<Metro> getMetros() {
        return metros;
    }

    public Location metros(Set<Metro> metros) {
        this.metros = metros;
        return this;
    }

    public Location addMetro(Metro metro) {
        this.metros.add(metro);
        metro.getLocations().add(this);
        return this;
    }

    public Location removeMetro(Metro metro) {
        this.metros.remove(metro);
        metro.getLocations().remove(this);
        return this;
    }

    public void setMetros(Set<Metro> metros) {
        this.metros = metros;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Location)) {
            return false;
        }
        return id != null && id.equals(((Location) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Location{" +
            "id=" + getId() +
            ", address='" + getAddress() + "'" +
            ", addressExplanation='" + getAddressExplanation() + "'" +
            "}";
    }
}
