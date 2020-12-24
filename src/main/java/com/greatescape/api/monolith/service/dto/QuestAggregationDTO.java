package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.QuestAggregation;
import com.greatescape.api.monolith.domain.enumeration.FearLevel;
import com.greatescape.api.monolith.domain.enumeration.QuestComplexity;
import com.greatescape.api.monolith.domain.enumeration.QuestType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Lob;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for the {@link QuestAggregation} entity.
 */
@Getter
@Setter
public final class QuestAggregationDTO implements Serializable {

    private UUID id;

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
    @Min(value = 0)
    private Integer minPrice;

    @NotNull
    @Min(value = 0)
    private Integer discountInPercents;

    @NotNull
    private QuestComplexity complexity;

    @NotNull
    private FearLevel fearLevel;

    @NotNull
    private QuestType type;

    private UUID locationId;

    private String locationAddress;

    private UUID companyId;

    private String companyTitle;

    private String coverImage;

    private Set<ThematicDTO> thematics = new HashSet<>();

    private Set<MetroDTO> metros = new HashSet<>();

    private Set<QuestPhotoDTO> photos = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestAggregationDTO)) {
            return false;
        }

        return id != null && id.equals(((QuestAggregationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "QuestAggregationDTO{" +
            "id=" + getId() +
            ", slug='" + getSlug() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", playersMinCount=" + getPlayersMinCount() +
            ", playersMaxCount=" + getPlayersMaxCount() +
            ", durationInMinutes=" + getDurationInMinutes() +
            ", minPrice=" + getMinPrice() +
            ", discountInPercents=" + getDiscountInPercents() +
            ", complexity='" + getComplexity() + "'" +
            ", fearLevel='" + getFearLevel() + "'" +
            ", type='" + getType() + "'" +
            ", locationId=" + getLocationId() +
            ", locationAddress='" + getLocationAddress() + "'" +
            ", companyId=" + getCompanyId() +
            ", companyTitle='" + getCompanyTitle() + "'" +
            ", thematics='" + getThematics() + "'" +
            ", metros='" + getMetros() + "'" +
            "}";
    }
}
