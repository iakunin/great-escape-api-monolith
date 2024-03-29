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
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Thematic.
 */
@Entity
@Table(name = "thematic")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
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
    @ToString.Exclude
    private Set<Quest> quests = new HashSet<>();

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
        // For more info see: https://bit.ly/37Zo2W3
        return getClass().hashCode();
    }
}
