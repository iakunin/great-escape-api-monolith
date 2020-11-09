package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.Quest;
import com.greatescape.api.monolith.domain.enumeration.FearLevel;
import com.greatescape.api.monolith.domain.enumeration.QuestComplexity;
import com.greatescape.api.monolith.domain.enumeration.QuestType;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for the {@link Quest} entity.
 */
@ApiModel(description = "@TODO: should be audited")
@Getter
@Setter
public final class QuestDTO implements Serializable {

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
