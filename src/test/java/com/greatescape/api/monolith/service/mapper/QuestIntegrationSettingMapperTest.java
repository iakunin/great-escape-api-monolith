package com.greatescape.api.monolith.service.mapper;

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
        Long id = 1L;
        Assertions.assertThat(questIntegrationSettingMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(questIntegrationSettingMapper.fromId(null)).isNull();
    }
}
