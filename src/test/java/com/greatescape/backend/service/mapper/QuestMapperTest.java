package com.greatescape.backend.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
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
        assertThat(questMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(questMapper.fromId(null)).isNull();
    }
}
