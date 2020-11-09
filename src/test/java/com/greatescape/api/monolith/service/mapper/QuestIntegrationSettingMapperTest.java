package com.greatescape.api.monolith.service.mapper;

import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QuestIntegrationSettingMapperTest {

    private QuestIntegrationSettingMapper questIntegrationSettingMapper;

    @BeforeEach
    public void setUp() {
        questIntegrationSettingMapper = new QuestIntegrationSettingMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        final UUID id = UUID.fromString("d92451a0-c19a-40c4-ba67-458973918a2b");
        Assertions.assertThat(questIntegrationSettingMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(questIntegrationSettingMapper.fromId(null)).isNull();
    }
}
