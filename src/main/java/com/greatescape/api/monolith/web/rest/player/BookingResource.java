package com.greatescape.api.monolith.web.rest.player;

import com.greatescape.api.monolith.config.ApplicationProperties;
import com.greatescape.api.monolith.config.Constants;
import com.greatescape.api.monolith.domain.Player;
import com.greatescape.api.monolith.repository.BookingRepository;
import com.greatescape.api.monolith.repository.PlayerRepository;
import com.greatescape.api.monolith.repository.SlotRepository;
import com.greatescape.api.monolith.service.BookingAdminService;
import com.greatescape.api.monolith.service.BookingPlayerService;
import com.greatescape.api.monolith.service.OtpService;
import com.greatescape.api.monolith.service.PlayerPlayerService;
import com.greatescape.api.monolith.service.dto.BookingDTO;
import com.greatescape.api.monolith.service.recaptcha.ReCaptcha;
import com.greatescape.api.monolith.web.rest.errors.EmailAlreadyUsedException;
import com.greatescape.api.monolith.web.rest.errors.ReCaptchaException;
import com.greatescape.api.monolith.web.rest.errors.SlotAlreadyBookedException;
import com.greatescape.api.monolith.web.rest.errors.SlotNotFoundException;
import com.greatescape.api.monolith.web.rest.errors.SlotTimeAlreadyPassedException;
import com.greatescape.api.monolith.web.rest.errors.SlotUnavailableForBookingException;
import com.greatescape.api.monolith.web.rest.errors.WrongOtpException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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

    private final BookingAdminService bookingAdminService;

    private final BookingPlayerService bookingPlayerService;

    private final BookingRepository bookingRepository;
    private final SlotRepository slotRepository;
    private final ApplicationProperties applicationProperties;

    private final PlayerPlayerService playerService;
    private final PlayerRepository playerRepository;

    private final OtpService otpService;

    private final ReCaptcha reCaptcha;

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
    public ResponseEntity<BookingPlayerService.CreateResponse> createBooking(
        @Valid @RequestBody CreateRequest request
    ) throws URISyntaxException {
        log.debug("REST request to create Booking : {}", request);

        try {
            this.reCaptcha.validate(request.getReCaptchaToken());
        } catch (ReCaptcha.InvalidReCaptchaException e) {
            throw new ReCaptchaException("booking");
        }

        request.setPhone(new Player.PhoneNormalized(request.getPhone()).value());
        request.setEmail(request.getEmail().toLowerCase());

        this.checkBookingBusinessRules(request);
        this.checkPlayerBusinessRules(request);

        if (request.isDryRun()) {
            return ResponseEntity.ok(
                new BookingPlayerService.CreateResponse(null, null, null, null)
            );
        }

        final var playerResponse = playerService.upsert(
            new PlayerPlayerService.CreateRequest(
                request.getName(),
                request.getPhone(),
                request.getEmail()
            )
        );

        final var result = bookingPlayerService.create(
            new BookingPlayerService.CreateRequest(
                request.getSlotId(),
                playerResponse.getPlayerId(),
                request.getComment()
            )
        );

        return ResponseEntity.created(new URI("/player-api/bookings/" + result.getBookingId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(
                    applicationName,
                    true,
                    ENTITY_NAME,
                    result.getBookingId().toString()
                )
            ).body(result);
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
        Optional<BookingDTO> bookingDTO = bookingAdminService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bookingDTO);
    }

    private void checkBookingBusinessRules(CreateRequest request) {
        final var slot = slotRepository.findById(request.getSlotId()).orElseThrow(
            () -> new SlotNotFoundException(request.getSlotId())
        );

        if (bookingRepository.existsBySlot(slot)) {
            throw new SlotAlreadyBookedException(slot.getId());
        }

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
    }

    private void checkPlayerBusinessRules(CreateRequest request) {
        if (
            playerRepository.findOneByPhone(request.getPhone()).isEmpty()
            && playerRepository.findOneByEmailIgnoreCase(request.getEmail()).isPresent()
        ) {
            throw new EmailAlreadyUsedException("booking");
        }

        if (!request.isDryRun()) {
            try {
                otpService.checkOtp(request.getPhone(), request.getOtp());
            } catch (OtpService.CheckOtpException e) {
                log.info("Wrong OTP='{}' for phone='{}'", request.getOtp(), request.getPhone());
                throw new WrongOtpException();
            }
        }
    }

    @Data
    @NoArgsConstructor
    public static final class CreateRequest {
        @NotBlank private String reCaptchaToken;
        @NotNull private UUID slotId;
        @NotBlank private String name;
        @NotBlank @Pattern(regexp = Constants.PHONE_REGEX) private String phone;
        @NotBlank private String otp;
        @NotBlank @Email private String email;
        private String comment;
        private boolean dryRun = false;
    }
}
