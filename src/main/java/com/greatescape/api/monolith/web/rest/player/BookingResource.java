package com.greatescape.api.monolith.web.rest.player;

import com.greatescape.api.monolith.domain.Player;
import com.greatescape.api.monolith.service.BookingAdminService;
import com.greatescape.api.monolith.service.BookingPlayerService;
import com.greatescape.api.monolith.service.dto.BookingDTO;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;
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
        request.setPhone(new Player.PhoneNormalized(request.getPhone()).value());

        // @TODO: create Player with base jHipster User
        //   and pass it to bookingService
        final var playerId = UUID.fromString("7d86b1d8-a7b2-4dc1-b3a0-7ad1e41ac348");

        final var result = bookingPlayerService.create(
            new BookingPlayerService.CreateRequest(
                request.getSlotId(),
                playerId,
                request.getName(),
                request.getPhone(),
                request.getEmail(),
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
}
