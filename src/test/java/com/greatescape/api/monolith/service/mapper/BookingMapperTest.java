package com.greatescape.api.monolith.service.mapper;

import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BookingMapperTest {

    private BookingMapper bookingMapper;

    @BeforeEach
    public void setUp() {
        bookingMapper = new BookingMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        final UUID id = UUID.fromString("060c0e15-55a1-4f51-a02b-14051a980305");
        Assertions.assertThat(bookingMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(bookingMapper.fromId(null)).isNull();
    }
}
