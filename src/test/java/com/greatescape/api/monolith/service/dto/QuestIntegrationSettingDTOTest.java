package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.web.rest.TestUtil;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class QuestIntegrationSettingDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestIntegrationSettingDTO.class);
        QuestIntegrationSettingDTO questIntegrationSettingDTO1 = new QuestIntegrationSettingDTO();
        questIntegrationSettingDTO1.setId(1L);
        QuestIntegrationSettingDTO questIntegrationSettingDTO2 = new QuestIntegrationSettingDTO();
        assertThat(questIntegrationSettingDTO1).isNotEqualTo(questIntegrationSettingDTO2);
        questIntegrationSettingDTO2.setId(questIntegrationSettingDTO1.getId());
        assertThat(questIntegrationSettingDTO1).isEqualTo(questIntegrationSettingDTO2);
        questIntegrationSettingDTO2.setId(2L);
        assertThat(questIntegrationSettingDTO1).isNotEqualTo(questIntegrationSettingDTO2);
        questIntegrationSettingDTO1.setId(null);
        assertThat(questIntegrationSettingDTO1).isNotEqualTo(questIntegrationSettingDTO2);
    }
}
