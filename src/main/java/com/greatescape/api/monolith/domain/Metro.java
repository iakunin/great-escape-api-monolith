package com.greatescape.api.monolith.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Metro.
 */
@Entity
@Table(name = "metro")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Metro extends AbstractEntity {

    @NotNull
    @Size(min = 2)
    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @NotNull
    @Size(min = 2)
    @Column(name = "title", nullable = false)
    private String title;

    @ManyToMany(mappedBy = "metros")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<Location> locations = new HashSet<>();

    public String getSlug() {
        return slug;
    }

    public Metro slug(String slug) {
        this.slug = slug;
        return this;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public Metro title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Location> getLocations() {
        return locations;
    }

    public Metro locations(Set<Location> locations) {
        this.locations = locations;
        return this;
    }

    public Metro addLocation(Location location) {
        this.locations.add(location);
        location.getMetros().add(this);
        return this;
    }

    public Metro removeLocation(Location location) {
        this.locations.remove(location);
        location.getMetros().remove(this);
        return this;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here
}
