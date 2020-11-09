package com.greatescape.api.monolith.service.mapper;

import java.util.UUID;
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
        final UUID id = UUID.fromString("d63a24e8-86b2-4c12-a4ae-ff7c326fbb7f");
        Assertions.assertThat(cityMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(cityMapper.fromId(null)).isNull();
    }
}
