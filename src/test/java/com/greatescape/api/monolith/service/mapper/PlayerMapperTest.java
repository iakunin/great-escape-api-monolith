package com.greatescape.api.monolith.service.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerMapperTest {

    private PlayerMapper playerMapper;

    @BeforeEach
    public void setUp() {
        playerMapper = new PlayerMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        Assertions.assertThat(playerMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(playerMapper.fromId(null)).isNull();
    }
}
