package com.greatescape.api.monolith.service;

import com.greatescape.api.monolith.domain.Thematic;
import com.greatescape.api.monolith.service.dto.ThematicDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Thematic}.
 */
public interface ThematicService {

    /**
     * Save a thematic.
     *
     * @param thematicDTO the entity to save.
     * @return the persisted entity.
     */
    ThematicDTO save(ThematicDTO thematicDTO);

    /**
     * Get all the thematics.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ThematicDTO> findAll(Pageable pageable);


    /**
     * Get the "id" thematic.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ThematicDTO> findOne(Long id);

    /**
     * Delete the "id" thematic.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
