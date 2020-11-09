package com.greatescape.api.monolith.service.mapper;

import java.util.UUID;
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
        final UUID id = UUID.fromString("5d457577-2546-4d92-85f2-7f03aee38bb1");
        Assertions.assertThat(thematicMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(thematicMapper.fromId(null)).isNull();
    }
}
