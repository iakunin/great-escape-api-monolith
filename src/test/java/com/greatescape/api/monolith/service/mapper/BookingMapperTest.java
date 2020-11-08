package com.greatescape.api.monolith.service.mapper;

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
        Long id = 1L;
        Assertions.assertThat(bookingMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(bookingMapper.fromId(null)).isNull();
    }
}
