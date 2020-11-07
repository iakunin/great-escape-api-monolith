package com.greatescape.backend.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
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
        assertThat(slotMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(slotMapper.fromId(null)).isNull();
    }
}
