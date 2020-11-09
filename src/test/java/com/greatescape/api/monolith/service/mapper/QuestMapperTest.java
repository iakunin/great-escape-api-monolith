package com.greatescape.api.monolith.service.mapper;

import java.util.UUID;
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
        final UUID id = UUID.fromString("1d68416c-3f19-4451-9bb9-788b9a3745e9");
        Assertions.assertThat(questMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(questMapper.fromId(null)).isNull();
    }
}
