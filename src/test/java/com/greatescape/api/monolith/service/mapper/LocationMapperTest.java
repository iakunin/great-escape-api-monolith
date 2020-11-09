package com.greatescape.api.monolith.service.mapper;

import java.util.UUID;
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
        final UUID id = UUID.fromString("8c54f236-7e2f-4dc1-9c40-73de77e65a04");
        Assertions.assertThat(locationMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(locationMapper.fromId(null)).isNull();
    }
}
