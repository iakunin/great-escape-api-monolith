package com.greatescape.api.monolith.service.impl.bookingplayerservice.integrationsender;

import com.greatescape.api.monolith.domain.Booking;
import com.greatescape.api.monolith.domain.QuestIntegrationSetting;
import com.greatescape.api.monolith.integration.BookFormClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class BookForm implements Sender {

    private final BookFormClient bookFormClient;

    @Override
    public void send(QuestIntegrationSetting setting, Booking booking) {
        final var settings = (QuestIntegrationSetting.BookForm) setting.getSettings();
        final var request = new BookFormClient.BookingRequest()
            .setName(booking.getPlayer().getName())
            .setEmail(booking.getPlayer().getEmail())
            .setPhone(booking.getPlayer().getPhone())
            .setComment(booking.getComment())
            .setService_id(settings.getServiceId())
            .setSource_id(settings.getWidgetId())
            .setSlots_id(booking.getSlot().getExternalId());

        log.info("Sending booking request to BookForm: {}", request);
        final var responseEntity = bookFormClient.createBooking(request);
        log.info("Got booking response from BookForm: {}", responseEntity);
    }
}
