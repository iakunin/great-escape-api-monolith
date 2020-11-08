package com.greatescape.api.monolith.service.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QuestMapperTest {

    private QuestMapper questMapper;

    @BeforeEach
    public void setUp() {
        questMapper = new QuestMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        Assertions.assertThat(questMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(questMapper.fromId(null)).isNull();
    }
}
