package com.greatescape.api.monolith.service.mapper;

import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SlotMapperTest {

    private SlotMapper slotMapper;

    @BeforeEach
    public void setUp() {
        slotMapper = new SlotMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        final UUID id = UUID.fromString("7fef425b-291e-4d29-83f0-55d50e142f65");
        Assertions.assertThat(slotMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(slotMapper.fromId(null)).isNull();
    }
}
