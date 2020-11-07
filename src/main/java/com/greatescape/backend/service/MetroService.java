package com.greatescape.backend.service;

import com.greatescape.backend.service.dto.MetroDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.greatescape.backend.domain.Metro}.
 */
public interface MetroService {

    /**
     * Save a metro.
     *
     * @param metroDTO the entity to save.
     * @return the persisted entity.
     */
    MetroDTO save(MetroDTO metroDTO);

    /**
     * Get all the metros.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MetroDTO> findAll(Pageable pageable);


    /**
     * Get the "id" metro.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MetroDTO> findOne(Long id);

    /**
     * Delete the "id" metro.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
