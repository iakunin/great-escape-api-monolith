package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.QuestIntegrationSetting;
import com.greatescape.api.monolith.domain.enumeration.QuestIntegrationType;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for the {@link QuestIntegrationSetting} entity.
 */
@Getter
@Setter
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
