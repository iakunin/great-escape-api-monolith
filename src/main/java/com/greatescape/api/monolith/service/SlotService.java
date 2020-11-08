package com.greatescape.api.monolith.service;

import com.greatescape.api.monolith.domain.Slot;
import com.greatescape.api.monolith.service.dto.SlotDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Slot}.
 */
public interface SlotService {

    /**
     * Save a slot.
     *
     * @param slotDTO the entity to save.
     * @return the persisted entity.
     */
    SlotDTO save(SlotDTO slotDTO);

    /**
     * Get all the slots.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SlotDTO> findAll(Pageable pageable);


    /**
     * Get the "id" slot.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SlotDTO> findOne(Long id);

    /**
     * Delete the "id" slot.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
