package com.greatescape.api.monolith.service.mapper;

import java.util.UUID;
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
        final UUID id = UUID.fromString("54a7262d-cdba-42b8-8945-d82296776402");
        Assertions.assertThat(questPhotoMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(questPhotoMapper.fromId(null)).isNull();
    }
}
