package com.greatescape.backend.service;

import com.greatescape.backend.service.dto.SubscriberDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.greatescape.backend.domain.Subscriber}.
 */
public interface SubscriberService {

    /**
     * Save a subscriber.
     *
     * @param subscriberDTO the entity to save.
     * @return the persisted entity.
     */
    SubscriberDTO save(SubscriberDTO subscriberDTO);

    /**
     * Get all the subscribers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SubscriberDTO> findAll(Pageable pageable);


    /**
     * Get the "id" subscriber.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubscriberDTO> findOne(Long id);

    /**
     * Delete the "id" subscriber.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
