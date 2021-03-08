package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.Quest;
import com.greatescape.api.monolith.domain.enumeration.FearLevel;
import com.greatescape.api.monolith.domain.enumeration.QuestComplexity;
import com.greatescape.api.monolith.domain.enumeration.QuestType;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Lob;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 * A DTO for the {@link Quest} entity.
 */
@Data
public final class QuestDTO {

    private UUID id;

    @NotNull
    @Size(min = 2)
    private String slug;

    @NotNull
    @Size(min = 2)
    private String title;

    @Lob
    private String description;

    @Lob
    private String details;

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

    private String coverPhoto;

    private UUID locationId;

    private String locationAddress;

    private UUID companyId;

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
        // For more info see: https://bit.ly/37Zo2W3
        return getClass().hashCode();
    }
}
