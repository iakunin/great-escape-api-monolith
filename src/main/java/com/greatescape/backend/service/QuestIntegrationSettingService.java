package com.greatescape.backend.service;

import com.greatescape.backend.service.dto.QuestIntegrationSettingDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.greatescape.backend.domain.QuestIntegrationSetting}.
 */
public interface QuestIntegrationSettingService {

    /**
     * Save a questIntegrationSetting.
     *
     * @param questIntegrationSettingDTO the entity to save.
     * @return the persisted entity.
     */
    QuestIntegrationSettingDTO save(QuestIntegrationSettingDTO questIntegrationSettingDTO);

    /**
     * Get all the questIntegrationSettings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<QuestIntegrationSettingDTO> findAll(Pageable pageable);


    /**
     * Get the "id" questIntegrationSetting.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<QuestIntegrationSettingDTO> findOne(Long id);

    /**
     * Delete the "id" questIntegrationSetting.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
