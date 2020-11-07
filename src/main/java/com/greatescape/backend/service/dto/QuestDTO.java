package com.greatescape.backend.service.dto;

import com.greatescape.backend.domain.enumeration.FearLevel;
import com.greatescape.backend.domain.enumeration.QuestComplexity;
import com.greatescape.backend.domain.enumeration.QuestType;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A DTO for the {@link com.greatescape.backend.domain.Quest} entity.
 */
@ApiModel(description = "@TODO: should be audited")
public class QuestDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2)
    private String slug;

    @NotNull
    @Size(min = 2)
    private String title;

    @Lob
    private String description;

    @NotNull
    @Min(value = 1)
    private Integer playersMinCount;

    @NotNull
    @Min(value = 1)
    private Integer playersMaxCount;

    @NotNull
    @Min(value = 1)
    private Integer durationInMinutes;

    @NotNull
    private QuestComplexity complexity;

    @NotNull
    private FearLevel fearLevel;

    @NotNull
    private QuestType type;


    private Long locationId;

    private String locationAddress;

    private Long companyId;

    private String companyTitle;
    private Set<ThematicDTO> thematics = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPlayersMinCount() {
        return playersMinCount;
    }

    public void setPlayersMinCount(Integer playersMinCount) {
        this.playersMinCount = playersMinCount;
    }

    public Integer getPlayersMaxCount() {
        return playersMaxCount;
    }

    public void setPlayersMaxCount(Integer playersMaxCount) {
        this.playersMaxCount = playersMaxCount;
    }

    public Integer getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(Integer durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public QuestComplexity getComplexity() {
        return complexity;
    }

    public void setComplexity(QuestComplexity complexity) {
        this.complexity = complexity;
    }

    public FearLevel getFearLevel() {
        return fearLevel;
    }

    public void setFearLevel(FearLevel fearLevel) {
        this.fearLevel = fearLevel;
    }

    public QuestType getType() {
        return type;
    }

    public void setType(QuestType type) {
        this.type = type;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyTitle() {
        return companyTitle;
    }

    public void setCompanyTitle(String companyTitle) {
        this.companyTitle = companyTitle;
    }

    public Set<ThematicDTO> getThematics() {
        return thematics;
    }

    public void setThematics(Set<ThematicDTO> thematics) {
        this.thematics = thematics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestDTO)) {
            return false;
        }

        return id != null && id.equals(((QuestDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestDTO{" +
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
            ", locationId=" + getLocationId() +
            ", locationAddress='" + getLocationAddress() + "'" +
            ", companyId=" + getCompanyId() +
            ", companyTitle='" + getCompanyTitle() + "'" +
            ", thematics='" + getThematics() + "'" +
            "}";
    }
}
