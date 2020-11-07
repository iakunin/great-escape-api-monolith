package com.greatescape.backend.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ThematicMapperTest {

    private ThematicMapper thematicMapper;

    @BeforeEach
    public void setUp() {
        thematicMapper = new ThematicMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(thematicMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(thematicMapper.fromId(null)).isNull();
    }
}
