package com.greatescape.api.monolith.service.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QuestPhotoMapperTest {

    private QuestPhotoMapper questPhotoMapper;

    @BeforeEach
    public void setUp() {
        questPhotoMapper = new QuestPhotoMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        Assertions.assertThat(questPhotoMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(questPhotoMapper.fromId(null)).isNull();
    }
}
