package com.greatescape.api.monolith.service.mapper;

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
        Long id = 1L;
        Assertions.assertThat(metroMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(metroMapper.fromId(null)).isNull();
    }
}
