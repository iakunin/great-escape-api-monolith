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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

@Entity
@Table(name = "quest")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Audited
@AuditOverride(forClass = AbstractEntity.class)
@Data
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

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "details")
    private String details;

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

    @NotNull
    @Size(min = 2)
    @Column(name = "cover_photo", nullable = false)
    private String coverPhoto;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "quests", allowSetters = true)
    private Location location;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
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
    @ToString.Exclude
    private Set<Thematic> thematics = new HashSet<>();

    @Column(name = "is_public")
    private Boolean isPublic;

    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "discount_in_percents")
    private Integer discountInPercents;

    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "commission_in_percents")
    private Integer commissionInPercents;

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
        // For more info see: https://bit.ly/37Zo2W3
        return getClass().hashCode();
    }
}
