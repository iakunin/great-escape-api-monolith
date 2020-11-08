package com.greatescape.api.monolith.service.mapper;

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
        Long id = 1L;
        Assertions.assertThat(slotMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(slotMapper.fromId(null)).isNull();
    }
}
