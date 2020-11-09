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
 * A Thematic.
 */
@Getter
@Setter
@Entity
@Table(name = "thematic")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Thematic extends AbstractEntity {

    @NotNull
    @Size(min = 2)
    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @NotNull
    @Size(min = 2)
    @Column(name = "title", nullable = false)
    private String title;

    @ManyToMany(mappedBy = "thematics")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<Quest> quests = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Thematic addQuest(Quest quest) {
        this.quests.add(quest);
        quest.getThematics().add(this);
        return this;
    }

    public Thematic removeQuest(Quest quest) {
        this.quests.remove(quest);
        quest.getThematics().remove(this);
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Thematic)) {
            return false;
        }
        return id != null && id.equals(((Thematic) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Thematic{" +
            "id=" + getId() +
            ", slug='" + getSlug() + "'" +
            ", title='" + getTitle() + "'" +
            "}";
    }
}
