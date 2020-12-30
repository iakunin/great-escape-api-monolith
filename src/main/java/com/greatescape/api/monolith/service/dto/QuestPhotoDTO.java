package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.QuestPhoto;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * A DTO for the {@link QuestPhoto} entity.
 */
@Data
public final class QuestPhotoDTO {

    private UUID id;

    @NotNull
    private String url;

    private UUID questId;

    private String questTitle;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestPhotoDTO)) {
            return false;
        }

        return id != null && id.equals(((QuestPhotoDTO) o).id);
    }

    @Override
    public int hashCode() {
        // For more info see: https://bit.ly/37Zo2W3
        return getClass().hashCode();
    }
}
