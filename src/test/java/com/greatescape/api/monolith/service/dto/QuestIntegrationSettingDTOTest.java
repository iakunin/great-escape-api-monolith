package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.web.rest.TestUtil;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class QuestIntegrationSettingDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestIntegrationSettingDTO.class);
        QuestIntegrationSettingDTO questIntegrationSettingDTO1 = new QuestIntegrationSettingDTO();
        questIntegrationSettingDTO1.setId(UUID.fromString("866bed36-0f1f-46bd-9710-c05232181368"));
        QuestIntegrationSettingDTO questIntegrationSettingDTO2 = new QuestIntegrationSettingDTO();
        assertThat(questIntegrationSettingDTO1).isNotEqualTo(questIntegrationSettingDTO2);
        questIntegrationSettingDTO2.setId(questIntegrationSettingDTO1.getId());
        assertThat(questIntegrationSettingDTO1).isEqualTo(questIntegrationSettingDTO2);
        questIntegrationSettingDTO2.setId(UUID.fromString("fdc9208c-fd0a-44e1-aa6c-9c75c79c552c"));
        assertThat(questIntegrationSettingDTO1).isNotEqualTo(questIntegrationSettingDTO2);
        questIntegrationSettingDTO1.setId(null);
        assertThat(questIntegrationSettingDTO1).isNotEqualTo(questIntegrationSettingDTO2);
    }
}
