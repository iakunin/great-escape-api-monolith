package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.QuestIntegrationSetting;
import com.greatescape.api.monolith.domain.enumeration.QuestIntegrationType;
import java.io.Serializable;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * A DTO for the {@link QuestIntegrationSetting} entity.
 */
@Data
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
        // For more info see: https://bit.ly/37Zo2W3
        return getClass().hashCode();
    }
}
