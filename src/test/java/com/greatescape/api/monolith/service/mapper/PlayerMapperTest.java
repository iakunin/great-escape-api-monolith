package com.greatescape.api.monolith.service.mapper;

import java.util.UUID;
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
        final UUID id = UUID.fromString("e001dec5-0d68-4d37-a244-ec3d985a429e");
        Assertions.assertThat(playerMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(playerMapper.fromId(null)).isNull();
    }
}
