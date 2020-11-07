package com.greatescape.backend.service.dto;

import java.io.Serializable;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link com.greatescape.backend.domain.QuestPhoto} entity.
 */
public class QuestPhotoDTO implements Serializable {

    private Long id;

    @NotNull
    private String url;


    private Long questId;

    private String questTitle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getQuestId() {
        return questId;
    }

    public void setQuestId(Long questId) {
        this.questId = questId;
    }

    public String getQuestTitle() {
        return questTitle;
    }

    public void setQuestTitle(String questTitle) {
        this.questTitle = questTitle;
    }

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
