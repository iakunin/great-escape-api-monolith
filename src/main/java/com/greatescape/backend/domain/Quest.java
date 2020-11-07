package com.greatescape.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.greatescape.backend.domain.enumeration.FearLevel;
import com.greatescape.backend.domain.enumeration.QuestComplexity;
import com.greatescape.backend.domain.enumeration.QuestType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * @TODO: should be audited
 */
@Entity
@Table(name = "quest")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Quest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

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
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public Quest slug(String slug) {
        this.slug = slug;
        return this;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public Quest title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public Quest description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPlayersMinCount() {
        return playersMinCount;
    }

    public Quest playersMinCount(Integer playersMinCount) {
        this.playersMinCount = playersMinCount;
        return this;
    }

    public void setPlayersMinCount(Integer playersMinCount) {
        this.playersMinCount = playersMinCount;
    }

    public Integer getPlayersMaxCount() {
        return playersMaxCount;
    }

    public Quest playersMaxCount(Integer playersMaxCount) {
        this.playersMaxCount = playersMaxCount;
        return this;
    }

    public void setPlayersMaxCount(Integer playersMaxCount) {
        this.playersMaxCount = playersMaxCount;
    }

    public Integer getDurationInMinutes() {
        return durationInMinutes;
    }

    public Quest durationInMinutes(Integer durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
        return this;
    }

    public void setDurationInMinutes(Integer durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public QuestComplexity getComplexity() {
        return complexity;
    }

    public Quest complexity(QuestComplexity complexity) {
        this.complexity = complexity;
        return this;
    }

    public void setComplexity(QuestComplexity complexity) {
        this.complexity = complexity;
    }

    public FearLevel getFearLevel() {
        return fearLevel;
    }

    public Quest fearLevel(FearLevel fearLevel) {
        this.fearLevel = fearLevel;
        return this;
    }

    public void setFearLevel(FearLevel fearLevel) {
        this.fearLevel = fearLevel;
    }

    public QuestType getType() {
        return type;
    }

    public Quest type(QuestType type) {
        this.type = type;
        return this;
    }

    public void setType(QuestType type) {
        this.type = type;
    }

    public Location getLocation() {
        return location;
    }

    public Quest location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Company getCompany() {
        return company;
    }

    public Quest company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Set<Thematic> getThematics() {
        return thematics;
    }

    public Quest thematics(Set<Thematic> thematics) {
        this.thematics = thematics;
        return this;
    }

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

    public void setThematics(Set<Thematic> thematics) {
        this.thematics = thematics;
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

    // prettier-ignore
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
