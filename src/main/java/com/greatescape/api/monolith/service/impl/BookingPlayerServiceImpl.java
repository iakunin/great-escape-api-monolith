package com.greatescape.api.monolith.service.impl;

import com.greatescape.api.monolith.config.ApplicationProperties;
import com.greatescape.api.monolith.domain.Booking;
import com.greatescape.api.monolith.domain.Player;
import com.greatescape.api.monolith.domain.QuestIntegrationSetting;
import com.greatescape.api.monolith.domain.Slot;
import com.greatescape.api.monolith.domain.enumeration.BookingStatus;
import com.greatescape.api.monolith.domain.enumeration.QuestIntegrationType;
import com.greatescape.api.monolith.integration.BookFormClient;
import com.greatescape.api.monolith.integration.MirKvestovClient;
import com.greatescape.api.monolith.repository.BookingRepository;
import com.greatescape.api.monolith.repository.PlayerRepository;
import com.greatescape.api.monolith.repository.QuestIntegrationSettingRepository;
import com.greatescape.api.monolith.repository.SlotAggregationRepository;
import com.greatescape.api.monolith.repository.SlotRepository;
import com.greatescape.api.monolith.service.BookingPlayerService;
import com.greatescape.api.monolith.web.rest.errors.PlayerNotFoundException;
import com.greatescape.api.monolith.web.rest.errors.SlotAlreadyBookedException;
import com.greatescape.api.monolith.web.rest.errors.SlotNotFoundException;
import com.greatescape.api.monolith.web.rest.errors.SlotTimeAlreadyPassedException;
import com.greatescape.api.monolith.web.rest.errors.SlotUnavailableForBookingException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingPlayerServiceImpl implements BookingPlayerService {

    private final BookingRepository bookingRepository;
    private final SlotRepository slotRepository;
    private final PlayerRepository playerRepository;
    private final ApplicationProperties applicationProperties;
    private final BookingCreator bookingCreator;
    private final IntegrationSender integrationSender;

    @Override
    public CreateResponse create(CreateRequest request) {
        this.checkBusinessRules(request);

        final var slot = slotRepository.getOne(request.getSlotId());
        final var player = playerRepository.getOne(request.getPlayerId());
        final var booking = bookingCreator.create(slot, player, request.getComment());

        integrationSender.send(booking);

        return new CreateResponse(
            booking.getId(),
            slot.getId(),
            slot.getQuest().getId(),
            player.getId()
        );
    }

    private void checkBusinessRules(CreateRequest request) {
        final var slot = slotRepository.findById(request.getSlotId()).orElseThrow(
            () -> new SlotNotFoundException(request.getSlotId())
        );

        if (!slot.getIsAvailable()) {
            throw new SlotUnavailableForBookingException(slot.getId());
        }

        if (ZonedDateTime.now()
            .isAfter(
                slot.getDateTimeWithTimeZone().plus(
                    applicationProperties.getSlot().getAvailabilityDelta()
                )
            )
        ) {
            throw new SlotTimeAlreadyPassedException(slot.getId());
        }

        if (bookingRepository.existsBySlot(slot)) {
            throw new SlotAlreadyBookedException(slot.getId());
        }

        if (!playerRepository.existsById(request.getPlayerId())) {
            throw new PlayerNotFoundException(request.getPlayerId());
        }
    }

    @Slf4j
    @RequiredArgsConstructor
    @Service
    public static class BookingCreator {

        private final SlotAggregationRepository slotAggregationRepository;

        private final BookingRepository bookingRepository;

        @Transactional(propagation = Propagation.REQUIRES_NEW)
        public Booking create(Slot slot, Player player, String comment) {
            final var slotAggregation = slotAggregationRepository.getOne(slot.getId());
            return bookingRepository.save(
                new Booking()
                    .setStatus(BookingStatus.NEW)
                    .setPrice(slot.getPrice())
                    .setDiscountInPercents(slotAggregation.getDiscountInPercents())
                    .setCommissionInPercents(slotAggregation.getCommissionInPercents())
                    .setComment(comment)
                    .setSlot(slot)
                    .setQuest(slot.getQuest())
                    .setPlayer(player)
            );
        }
    }

    @Slf4j
    @RequiredArgsConstructor
    @Service
    public static class IntegrationSender {

        private final QuestIntegrationSettingRepository questIntegrationSettingRepository;

        private final BookFormClient bookFormClient;

        private final MirKvestovClient mirKvestovClient;

        // @TODO: maybe it should be sent via some queue (which one?)
        public void send(Booking booking) {
            final var slot = booking.getSlot();
            final var player = booking.getPlayer();

            final var integrationSetting = questIntegrationSettingRepository
                .findOneByQuest(slot.getQuest())
                .orElseThrow();

            if (integrationSetting.getType() == QuestIntegrationType.BOOK_FORM) {
                final var settings = (QuestIntegrationSetting.BookForm) integrationSetting.getSettings();
                bookFormClient.createBooking(
                    new BookFormClient.BookingRequest()
                        .setName(player.getName())
                        .setEmail(player.getEmail())
                        .setPhone(player.getPhone())
                        .setComment(booking.getComment())
                        .setService_id(settings.getServiceId())
                        .setSource_id(settings.getWidgetId())
                        .setSlots_id(slot.getExternalId())
                );
            } else if (integrationSetting.getType() == QuestIntegrationType.MIR_KVESTOV) {
                final var settings = (QuestIntegrationSetting.MirKvestov) integrationSetting.getSettings();
                mirKvestovClient.createBooking(
                    new MirKvestovClient.BookingRequest()
                        .setFirstName(player.getName())
                        .setFamilyName(".")
                        .setPhone(player.getPhone())
                        .setEmail(player.getEmail())
                        .setComment(booking.getComment())
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
                        .setPrice(slot.getPrice())
                    ,
                    settings.getBookingUrl()
                );
            } else {
                throw new RuntimeException(
                    String.format("Got unsupported integration type '%s'", integrationSetting.getType().toString())
                );
            }
        }
    }
}
