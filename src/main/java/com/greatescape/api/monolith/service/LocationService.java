package com.greatescape.api.monolith.service;

import com.greatescape.api.monolith.domain.Location;
import com.greatescape.api.monolith.service.dto.LocationDTO;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Location}.
 */
public interface LocationService {

    /**
     * Save a location.
     *
     * @param locationDTO the entity to save.
     * @return the persisted entity.
     */
    LocationDTO save(LocationDTO locationDTO);

    /**
     * Get all the locations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LocationDTO> findAll(Pageable pageable);

    /**
     * Get all the locations with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<LocationDTO> findAllWithEagerRelationships(Pageable pageable);


    /**
     * Get the "id" location.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LocationDTO> findOne(UUID id);

    /**
     * Delete the "id" location.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
