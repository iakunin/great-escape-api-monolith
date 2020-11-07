package com.greatescape.backend.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
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
        assertThat(cityMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(cityMapper.fromId(null)).isNull();
    }
}
