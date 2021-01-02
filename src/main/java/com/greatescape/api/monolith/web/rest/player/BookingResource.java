package com.greatescape.api.monolith.web.rest.player;

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
import com.greatescape.api.monolith.service.BookingService;
import com.greatescape.api.monolith.service.dto.BookingDTO;
import com.greatescape.api.monolith.service.mapper.BookingMapper;
import com.greatescape.api.monolith.web.rest.errors.SlotAlreadyBookedException;
import com.greatescape.api.monolith.web.rest.errors.SlotNotFoundException;
import com.greatescape.api.monolith.web.rest.errors.SlotTimeAlreadyPassedException;
import com.greatescape.api.monolith.web.rest.errors.SlotUnavailableForBookingException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("player.BookingResource")
@RequestMapping("/player-api")
@RequiredArgsConstructor
@Slf4j
public class BookingResource {

    private static final String ENTITY_NAME = "booking";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApplicationProperties applicationProperties;

    private final BookingService bookingService;

    private final SlotRepository slotRepository;

    private final BookingRepository bookingRepository;

    private final BookingCreator bookingCreator;

    private final IntegrationSender integrationSender;

    private final BookingMapper bookingMapper;

    /**
     * {@code POST  /bookings} : Create a new booking.
     *
     * @param request the request to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
     *   request, or with status {@code 400 (Bad Request)} when some business rule violation occurred.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bookings")
    @Transactional
    public ResponseEntity<BookingDTO> createBooking(@Valid @RequestBody CreateRequest request) throws URISyntaxException {
        log.debug("REST request to create Booking : {}", request);

        request.setPhone(
            new Player.PhoneNormalized(request.getPhone()).value()
        );

        this.checkBusinessRules(request);

        final var slot = slotRepository.getOne(request.getSlotId());

        final var booking = bookingCreator.create(slot);

        integrationSender.send(request, slot);

        // @TODO: get rid of `commissionInPercents` from BookingDTO (create custom BookingDTO)
        BookingDTO result = bookingMapper.toDto(booking);

        return ResponseEntity.created(new URI("/player-api/bookings/" + result.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(
                    applicationName,
                    true,
                    ENTITY_NAME,
                    result.getId().toString()
                )
            ).body(result);
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
    }

    /**
     * {@code GET  /bookings/:id} : get the "id" booking.
     *
     * @param id the id of the bookingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bookingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bookings/{id}")
    public ResponseEntity<BookingDTO> getBooking(@PathVariable UUID id) {
        log.debug("REST request to get Booking : {}", id);
        Optional<BookingDTO> bookingDTO = bookingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bookingDTO);
    }

    @Data
    public static final class CreateRequest {
        @NotNull
        private UUID slotId;

        @NotNull
        private String name;

        @NotNull
        private String phone;

        @NotNull
        private String email;

        private String comment;
    }

    @Slf4j
    @RequiredArgsConstructor
    @Service
    public static class IntegrationSender {

        private final QuestIntegrationSettingRepository questIntegrationSettingRepository;

        private final BookFormClient bookFormClient;

        private final MirKvestovClient mirKvestovClient;

        // @TODO: maybe it should be sent via some queue (which one?)
        public void send(CreateRequest request, Slot slot) {
            final var integrationSetting = questIntegrationSettingRepository
                .findOneByQuest(slot.getQuest())
                .orElseThrow();

            if (integrationSetting.getType() == QuestIntegrationType.BOOK_FORM) {
                final var settings = (QuestIntegrationSetting.BookForm) integrationSetting.getSettings();
                bookFormClient.createBooking(
                    new BookFormClient.BookingRequest()
                        .setName(request.getName())
                        .setEmail(request.getEmail())
                        .setPhone(request.getPhone())
                        .setComment(request.getComment())
                        .setService_id(settings.getServiceId())
                        .setSource_id(settings.getWidgetId())
                        .setSlots_id(slot.getExternalId())
                );
            } else if (integrationSetting.getType() == QuestIntegrationType.MIR_KVESTOV) {
                final var settings = (QuestIntegrationSetting.MirKvestov) integrationSetting.getSettings();
                mirKvestovClient.createBooking(
                    new MirKvestovClient.BookingRequest()
                        .setFirstName(request.getName())
                        .setFamilyName(".")
                        .setPhone(request.getPhone())
                        .setEmail(request.getEmail())
                        .setComment(request.getComment())
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

    @Slf4j
    @RequiredArgsConstructor
    @Service
    public static class BookingCreator {

        private final SlotAggregationRepository slotAggregationRepository;

        private final BookingRepository bookingRepository;

        private final PlayerRepository playerRepository;

        @Transactional(propagation = Propagation.REQUIRES_NEW)
        public Booking create(Slot slot) {

            // @TODO: create Player (in the same Transaction) - with base jHipster User

            final var slotAggregation = slotAggregationRepository.getOne(slot.getId());
            return bookingRepository.save(
                new Booking()
                    .setStatus(BookingStatus.NEW)
                    .setPrice(slot.getPrice())
                    .setDiscountInPercents(slotAggregation.getDiscountInPercents())
                    .setCommissionInPercents(slotAggregation.getCommissionInPercents())
                    .setSlot(slot)
                    .setQuest(slot.getQuest())
                    .setPlayer(
                        playerRepository.getOne(
                            UUID.fromString("7d86b1d8-a7b2-4dc1-b3a0-7ad1e41ac348")
                        )
                    )
            );
        }
    }
}
