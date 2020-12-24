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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.envers.NotAudited;

@Getter
@Setter
@Entity
@Table(name = "quest_aggregation")
public class QuestAggregation extends AbstractEntity {

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

    @NotAudited
    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "quest_thematic",
        joinColumns = @JoinColumn(name = "quest_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "thematic_id", referencedColumnName = "id"))
    private Set<Thematic> thematics = new HashSet<>();

    @NotNull
    @Min(value = 1)
    @Column(name = "min_price", nullable = false)
    private Integer minPrice;

    @NotNull
    @Min(value = 0)
    @Column(name = "discount_in_percents", nullable = false)
    private Integer discountInPercents;

    private String coverImage;

    @OneToMany
    @JoinColumn(name = "quest_id")
    private Set<QuestPhoto> photos = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestAggregation)) {
            return false;
        }
        return id != null && id.equals(((QuestAggregation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "QuestAggregation{" +
            "id=" + id +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            ", slug='" + slug + '\'' +
            ", title='" + title + '\'' +
            ", description='" + description + '\'' +
            ", playersMinCount=" + playersMinCount +
            ", playersMaxCount=" + playersMaxCount +
            ", durationInMinutes=" + durationInMinutes +
            ", complexity=" + complexity +
            ", fearLevel=" + fearLevel +
            ", type=" + type +
            ", location=" + location +
            ", company=" + company +
            ", thematics=" + thematics +
            ", minPrice=" + minPrice +
            ", discountInPercents=" + discountInPercents +
            ", coverImage='" + coverImage + '\'' +
            '}';
    }
}
