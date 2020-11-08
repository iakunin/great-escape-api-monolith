package com.greatescape.api.monolith.service.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CityMapperTest {

    private CityMapper cityMapper;

    @BeforeEach
    public void setUp() {
        cityMapper = new CityMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        Assertions.assertThat(cityMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(cityMapper.fromId(null)).isNull();
    }
}
