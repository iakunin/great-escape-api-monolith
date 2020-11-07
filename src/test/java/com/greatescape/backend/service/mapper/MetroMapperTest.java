package com.greatescape.backend.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
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
        assertThat(metroMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(metroMapper.fromId(null)).isNull();
    }
}
