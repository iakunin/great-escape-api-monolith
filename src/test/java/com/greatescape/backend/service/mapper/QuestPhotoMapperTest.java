package com.greatescape.backend.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
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
        assertThat(questPhotoMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(questPhotoMapper.fromId(null)).isNull();
    }
}
