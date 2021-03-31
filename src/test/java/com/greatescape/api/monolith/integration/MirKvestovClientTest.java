package com.greatescape.api.monolith.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class MirKvestovClientTest {

    @Test
    public void bookingRequestSignatureBuilderNonEmptySecret() {
        final var actual = new MirKvestovClient.BookingRequestSignatureBuilder().build(
            new MirKvestovClient.BookingRequest()
                .setFirstName("Иван")
                .setFamilyName("Петров")
                .setPhone("+7 (999) 111-11-11")
                .setEmail("test@mail.ru"),
            "mir-kvestov.ru"
        );

        assertEquals("d70c64986647d82ea5d88a5a3cd72dce", actual);
    }

    @Test
    public void bookingRequestSignatureBuilderEmptySecret() {
        final var actual = new MirKvestovClient.BookingRequestSignatureBuilder().build(
            new MirKvestovClient.BookingRequest()
                .setFirstName("Иван")
                .setFamilyName("Петров")
                .setPhone("+7 (926) 111-11-11")
                .setEmail("test@mail.ru"),
            ""
        );

        assertEquals("8d5ffc786e882c09f91c46dd84bc9464", actual);
    }


    @Test
    public void bookingRequestSignatureBuilderNullSecret() {
        final var actual = new MirKvestovClient.BookingRequestSignatureBuilder().build(
            new MirKvestovClient.BookingRequest()
                .setFirstName("Иван")
                .setFamilyName("Петров")
                .setPhone("+7 (926) 111-11-11")
                .setEmail("test@mail.ru"),
            null
        );

        assertEquals("8d5ffc786e882c09f91c46dd84bc9464", actual);
    }
}
