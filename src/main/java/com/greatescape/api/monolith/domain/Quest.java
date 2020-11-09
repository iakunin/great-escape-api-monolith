package com.greatescape.api.monolith.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.greatescape.api.monolith.domain.enumeration.FearLevel;
import com.greatescape.api.monolith.domain.enumeration.QuestComplexity;
import com.greatescape.api.monolith.domain.enumeration.QuestType;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * @TODO: should be audited
 */
@Getter
@Setter
@Entity
@Table(name = "quest")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Quest extends AbstractEntity {

    @NotNull
    @Size(min = 2)
    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @NotNull
    @Size(min = 2)
    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description")
    private String description;

    @NotNull
    @Min(value = 1)
    @Column(name = "players_min_count", nullable = false)
    private Integer playersMinCount;

    @NotNull
    @Min(value = 1)
    @Column(name = "players_max_count", nullable = false)
    private Integer playersMaxCount;

    @NotNull
    @Min(value = 1)
    @Column(name = "duration_in_minutes", nullable = false)
    private Integer durationInMinutes;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "complexity", nullable = false)
    private QuestComplexity complexity;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "fear_level", nullable = false)
    private FearLevel fearLevel;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private QuestType type;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "quests", allowSetters = true)
    private Location location;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "quests", allowSetters = true)
    private Company company;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "quest_thematic",
               joinColumns = @JoinColumn(name = "quest_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "thematic_id", referencedColumnName = "id"))
    private Set<Thematic> thematics = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Quest addThematic(Thematic thematic) {
        this.thematics.add(thematic);
        thematic.getQuests().add(this);
        return this;
    }

    public Quest removeThematic(Thematic thematic) {
        this.thematics.remove(thematic);
        thematic.getQuests().remove(this);
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Quest)) {
            return false;
        }
        return id != null && id.equals(((Quest) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Quest{" +
            "id=" + getId() +
            ", slug='" + getSlug() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", playersMinCount=" + getPlayersMinCount() +
            ", playersMaxCount=" + getPlayersMaxCount() +
            ", durationInMinutes=" + getDurationInMinutes() +
            ", complexity='" + getComplexity() + "'" +
            ", fearLevel='" + getFearLevel() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
