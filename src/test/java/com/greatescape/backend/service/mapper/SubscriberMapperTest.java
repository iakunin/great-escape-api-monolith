package com.greatescape.backend.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
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
        assertThat(subscriberMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(subscriberMapper.fromId(null)).isNull();
    }
}
