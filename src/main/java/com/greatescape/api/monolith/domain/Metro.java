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
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Metro.
 */
@Getter
@Setter
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Metro)) {
            return false;
        }
        return id != null && id.equals(((Metro) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Metro{" +
            "id=" + getId() +
            ", slug='" + getSlug() + "'" +
            ", title='" + getTitle() + "'" +
            "}";
    }
}
