package com.greatescape.api.monolith.service.impl.bookingplayerservice.integrationsender;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greatescape.api.monolith.domain.Booking;
import com.greatescape.api.monolith.domain.QuestIntegrationSetting;
import com.greatescape.api.monolith.integration.MirKvestovClient;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public final class MirKvestov implements Sender {

    private final MirKvestovClient mirKvestovClient;
    private final MirKvestovClient.BookingRequestSignatureBuilder signatureBuilder;
    private final ObjectMapper objectMapper;

    @Override
    public void send(QuestIntegrationSetting setting, Booking booking) {
        final var settings = (QuestIntegrationSetting.MirKvestov) setting.getSettings();
        final var request = new MirKvestovClient.BookingRequest()
            .setFirstName(booking.getPlayer().getName())
            .setFamilyName(".")
            .setPhone(booking.getPlayer().getPhone())
            .setEmail(booking.getPlayer().getEmail())
            .setComment(booking.getComment())
            .setSource("great-escape.ru")
            .setDate(
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    .withZone(ZoneId.of("Z"))
                    .format(booking.getSlot().getDateTimeLocal())
            )
            .setTime(
                DateTimeFormatter.ofPattern("HH:mm")
                    .withZone(ZoneId.of("Z"))
                    .format(booking.getSlot().getDateTimeLocal())
            )
            .setPrice(booking.getSlot().getPrice());
        request.setMd5(
            signatureBuilder.build(request, settings.getMd5key())
        );

        log.info("Sending booking request to MirKvestov: {}", request);

        final var responseEntity = mirKvestovClient.createBooking(
            this.buildRequestBody(request, booking.getSlot().getExternalState()),
            settings.getBookingUrl()
        );

        log.info("Got booking response from MirKvestov: {}", responseEntity);

        if (responseEntity.getBody() == null) {
            log.warn("Got empty body response from MirKvestov: {}", responseEntity);
            throw new RuntimeException("Got empty body response from MirKvestov");
        }

        if (!responseEntity.getBody().isSuccess()) {
            log.warn("Got unsuccessful response from MirKvestov: {}", responseEntity);
            throw new RuntimeException("Got unsuccessful response from MirKvestov");
        }
    }

    private Map<String, Object> buildRequestBody(
        MirKvestovClient.BookingRequest request,
        Map<String, Object> externalState
    ) {
        final var result = new HashMap<>(
            Optional.ofNullable(externalState).orElse(new HashMap<>())
        );
        result.putAll(
            objectMapper.convertValue(request, new TypeReference<Map<String, Object>>() {
            })
        );

        log.info("RequestBody: {}", result);

        return result;
    }
}
