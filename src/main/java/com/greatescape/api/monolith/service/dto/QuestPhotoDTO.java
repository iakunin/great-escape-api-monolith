package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.QuestPhoto;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for the {@link QuestPhoto} entity.
 */
@Getter
@Setter
public class QuestPhotoDTO implements Serializable {

    private Long id;

    @NotNull
    private String url;

    private Long questId;

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
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestPhotoDTO{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            ", questId=" + getQuestId() +
            ", questTitle='" + getQuestTitle() + "'" +
            "}";
    }
}
