package com.greatescape.api.monolith.service.mapper;

import java.util.UUID;
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
        final UUID id = UUID.fromString("96457916-cf32-4cc9-ab00-5dea55040853");
        Assertions.assertThat(subscriberMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(subscriberMapper.fromId(null)).isNull();
    }
}
