package com.greatescape.api.monolith.service.mapper;

import org.assertj.core.api.Assertions;
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
        Assertions.assertThat(thematicMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(thematicMapper.fromId(null)).isNull();
    }
}
