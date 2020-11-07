package com.greatescape.backend.service.dto;

import com.greatescape.backend.domain.enumeration.QuestIntegrationType;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link com.greatescape.backend.domain.QuestIntegrationSetting} entity.
 */
public class QuestIntegrationSettingDTO implements Serializable {

    private Long id;

    @NotNull
    private QuestIntegrationType type;

    /**
     * @TODO: convert to Json-field
     */
    @ApiModelProperty(value = "@TODO: convert to Json-field")
    @Lob
    private String settings;


    private Long questId;

    private String questTitle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuestIntegrationType getType() {
        return type;
    }

    public void setType(QuestIntegrationType type) {
        this.type = type;
    }

    public String getSettings() {
        return settings;
    }

    public void setSettings(String settings) {
        this.settings = settings;
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
        if (!(o instanceof QuestIntegrationSettingDTO)) {
            return false;
        }

        return id != null && id.equals(((QuestIntegrationSettingDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestIntegrationSettingDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", settings='" + getSettings() + "'" +
            ", questId=" + getQuestId() +
            ", questTitle='" + getQuestTitle() + "'" +
            "}";
    }
}
