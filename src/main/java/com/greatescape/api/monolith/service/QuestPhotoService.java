package com.greatescape.api.monolith.service;

import com.greatescape.api.monolith.domain.QuestPhoto;
import com.greatescape.api.monolith.service.dto.QuestPhotoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link QuestPhoto}.
 */
public interface QuestPhotoService {

    /**
     * Save a questPhoto.
     *
     * @param questPhotoDTO the entity to save.
     * @return the persisted entity.
     */
    QuestPhotoDTO save(QuestPhotoDTO questPhotoDTO);

    /**
     * Get all the questPhotos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<QuestPhotoDTO> findAll(Pageable pageable);


    /**
     * Get the "id" questPhoto.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<QuestPhotoDTO> findOne(Long id);

    /**
     * Delete the "id" questPhoto.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
