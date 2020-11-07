package com.greatescape.backend.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
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
        assertThat(questIntegrationSettingMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(questIntegrationSettingMapper.fromId(null)).isNull();
    }
}
