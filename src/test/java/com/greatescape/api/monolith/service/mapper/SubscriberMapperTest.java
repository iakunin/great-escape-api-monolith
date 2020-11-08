package com.greatescape.api.monolith.service.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SubscriberMapperTest {

    private SubscriberMapper subscriberMapper;

    @BeforeEach
    public void setUp() {
        subscriberMapper = new SubscriberMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        Assertions.assertThat(subscriberMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(subscriberMapper.fromId(null)).isNull();
    }
}
