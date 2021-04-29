package com.greatescape.api.monolith.service.impl.bookingplayerservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greatescape.api.monolith.domain.Booking;
import com.greatescape.api.monolith.domain.QuestIntegrationSetting;
import com.greatescape.api.monolith.domain.enumeration.QuestIntegrationType;
import com.greatescape.api.monolith.integration.BookFormClient;
import com.greatescape.api.monolith.integration.MirKvestovClient;
import com.greatescape.api.monolith.repository.QuestIntegrationSettingRepository;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class IntegrationSender {

    private final QuestIntegrationSettingRepository questIntegrationSettingRepository;
    private final BookFormClient bookFormClient;
    private final MirKvestovClient mirKvestovClient;
    private final MirKvestovClient.BookingRequestSignatureBuilder signatureBuilder;
    private final ObjectMapper objectMapper;

    public void send(Booking booking) {
        final var slot = booking.getSlot();
        final var player = booking.getPlayer();

        final var integrationSetting = questIntegrationSettingRepository
            .findOneByQuest(slot.getQuest())
            .orElseThrow();

        if (integrationSetting.getType() == QuestIntegrationType.BOOK_FORM) {
            final var settings = (QuestIntegrationSetting.BookForm) integrationSetting.getSettings();
            final var request = new BookFormClient.BookingRequest()
                .setName(player.getName())
                .setEmail(player.getEmail())
                .setPhone(player.getPhone())
                .setComment(booking.getComment())
                .setService_id(settings.getServiceId())
                .setSource_id(settings.getWidgetId())
                .setSlots_id(slot.getExternalId());

            log.info("Sending booking request to BookForm: {}", request);
            final var responseEntity = bookFormClient.createBooking(request);
            log.info("Got booking response from BookForm: {}", responseEntity);
        } else if (integrationSetting.getType() == QuestIntegrationType.MIR_KVESTOV) {
            final var settings = (QuestIntegrationSetting.MirKvestov) integrationSetting.getSettings();
            final var request = new MirKvestovClient.BookingRequest()
                .setFirstName(player.getName())
                .setFamilyName(".")
                .setPhone(player.getPhone())
                .setEmail(player.getEmail())
                .setComment(booking.getComment())
                .setSource("great-escape.ru")
                .setDate(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        .withZone(ZoneId.of("Z"))
                        .format(slot.getDateTimeLocal())
                )
                .setTime(
                    DateTimeFormatter.ofPattern("HH:mm")
                        .withZone(ZoneId.of("Z"))
                        .format(slot.getDateTimeLocal())
                )
                .setPrice(slot.getPrice());
            request.setMd5(
                signatureBuilder.build(request, settings.getMd5key())
            );

            log.info("Sending booking request to MirKvestov: {}", request);

            final var responseEntity = mirKvestovClient.createBooking(
                this.buildRequestBody(request, slot.getExternalState()),
                settings.getBookingUrl()
            );

            log.info("Got booking response from MirKvestov: {}", responseEntity);

            if (!responseEntity.getBody().isSuccess()) {
                log.warn("Got unsuccessful response from MirKvestov: {}", responseEntity);
                throw new RuntimeException("Got unsuccessful response from MirKvestov");
            }
        } else {
            throw new RuntimeException(
                String.format("Got unsupported integration type '%s'", integrationSetting.getType().toString())
            );
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
