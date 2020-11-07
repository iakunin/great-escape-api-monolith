package com.greatescape.backend.domain;

import com.greatescape.backend.web.rest.TestUtil;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class QuestIntegrationSettingTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestIntegrationSetting.class);
        QuestIntegrationSetting questIntegrationSetting1 = new QuestIntegrationSetting();
        questIntegrationSetting1.setId(1L);
        QuestIntegrationSetting questIntegrationSetting2 = new QuestIntegrationSetting();
        questIntegrationSetting2.setId(questIntegrationSetting1.getId());
        assertThat(questIntegrationSetting1).isEqualTo(questIntegrationSetting2);
        questIntegrationSetting2.setId(2L);
        assertThat(questIntegrationSetting1).isNotEqualTo(questIntegrationSetting2);
        questIntegrationSetting1.setId(null);
        assertThat(questIntegrationSetting1).isNotEqualTo(questIntegrationSetting2);
    }
}
