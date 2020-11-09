package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.web.rest.TestUtil;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class BookingDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookingDTO.class);
        BookingDTO bookingDTO1 = new BookingDTO();
        bookingDTO1.setId(UUID.fromString("c9315756-38d7-44e8-9a5b-fca0b7cadc47"));
        BookingDTO bookingDTO2 = new BookingDTO();
        assertThat(bookingDTO1).isNotEqualTo(bookingDTO2);
        bookingDTO2.setId(bookingDTO1.getId());
        assertThat(bookingDTO1).isEqualTo(bookingDTO2);
        bookingDTO2.setId(UUID.fromString("5a167782-eba4-4c78-9d38-d6a02015ec94"));
        assertThat(bookingDTO1).isNotEqualTo(bookingDTO2);
        bookingDTO1.setId(null);
        assertThat(bookingDTO1).isNotEqualTo(bookingDTO2);
    }
}
