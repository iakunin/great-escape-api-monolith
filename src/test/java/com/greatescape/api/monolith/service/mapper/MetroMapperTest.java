package com.greatescape.api.monolith.service.mapper;

import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MetroMapperTest {

    private MetroMapper metroMapper;

    @BeforeEach
    public void setUp() {
        metroMapper = new MetroMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        final UUID id = UUID.fromString("307b24ac-a6c6-4faa-927c-18c1efdfa6d9");
        Assertions.assertThat(metroMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(metroMapper.fromId(null)).isNull();
    }
}
