package com.greatescape.api.monolith.service;

import com.greatescape.api.monolith.domain.Booking;
import com.greatescape.api.monolith.service.dto.BookingDTO;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Booking}.
 */
public interface BookingAdminService {

    /**
     * Save a booking.
     *
     * @param bookingDTO the entity to save.
     * @return the persisted entity.
     */
    BookingDTO save(BookingDTO bookingDTO);

    /**
     * Get all the bookings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BookingDTO> findAll(Pageable pageable);


    /**
     * Get the "id" booking.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BookingDTO> findOne(UUID id);

    /**
     * Delete the "id" booking.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
