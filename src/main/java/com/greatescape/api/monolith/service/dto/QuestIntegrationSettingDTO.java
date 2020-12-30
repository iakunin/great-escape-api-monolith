package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.QuestIntegrationSetting;
import com.greatescape.api.monolith.domain.enumeration.QuestIntegrationType;
import java.io.Serializable;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for the {@link QuestIntegrationSetting} entity.
 */
@Getter
@Setter
public final class QuestIntegrationSettingDTO implements Serializable {

    private UUID id;

    @NotNull
    private QuestIntegrationType type;

    private QuestIntegrationSetting.AbstractSettings settings;

    private UUID questId;

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
