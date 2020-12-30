package com.greatescape.api.monolith.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Location.
 */
@Getter
@Setter
@Entity
@Table(name = "location")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Location extends AbstractEntity {

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

    @Override
    public String toString() {
        return "Location{" +
            "id=" + getId() +
            ", address='" + getAddress() + "'" +
            ", addressExplanation='" + getAddressExplanation() + "'" +
            "}";
    }
}
