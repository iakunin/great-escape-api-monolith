package com.greatescape.api.monolith.domain;

import com.greatescape.api.monolith.web.rest.TestUtil;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class QuestIntegrationSettingTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestIntegrationSetting.class);
        QuestIntegrationSetting questIntegrationSetting1 = new QuestIntegrationSetting();
        questIntegrationSetting1.setId(UUID.fromString("d591b84e-e73e-4a67-9982-feb37d9d5e63"));
        QuestIntegrationSetting questIntegrationSetting2 = new QuestIntegrationSetting();
        questIntegrationSetting2.setId(questIntegrationSetting1.getId());
        assertThat(questIntegrationSetting1).isEqualTo(questIntegrationSetting2);
        questIntegrationSetting2.setId(UUID.fromString("90d5aae1-70ab-47c2-a5e2-196cc9a6a62a"));
        assertThat(questIntegrationSetting1).isNotEqualTo(questIntegrationSetting2);
        questIntegrationSetting1.setId(null);
        assertThat(questIntegrationSetting1).isNotEqualTo(questIntegrationSetting2);
    }
}
