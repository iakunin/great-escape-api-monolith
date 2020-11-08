package com.greatescape.api.monolith.service.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LocationMapperTest {

    private LocationMapper locationMapper;

    @BeforeEach
    public void setUp() {
        locationMapper = new LocationMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        Assertions.assertThat(locationMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(locationMapper.fromId(null)).isNull();
    }
}
