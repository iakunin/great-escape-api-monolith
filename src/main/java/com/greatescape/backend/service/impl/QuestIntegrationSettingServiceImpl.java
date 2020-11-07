package com.greatescape.backend.service.impl;

import com.greatescape.backend.domain.QuestIntegrationSetting;
import com.greatescape.backend.repository.QuestIntegrationSettingRepository;
import com.greatescape.backend.service.QuestIntegrationSettingService;
import com.greatescape.backend.service.dto.QuestIntegrationSettingDTO;
import com.greatescape.backend.service.mapper.QuestIntegrationSettingMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link QuestIntegrationSetting}.
 */
@Service
@Transactional
public class QuestIntegrationSettingServiceImpl implements QuestIntegrationSettingService {

    private final Logger log = LoggerFactory.getLogger(QuestIntegrationSettingServiceImpl.class);

    private final QuestIntegrationSettingRepository questIntegrationSettingRepository;

    private final QuestIntegrationSettingMapper questIntegrationSettingMapper;

    public QuestIntegrationSettingServiceImpl(QuestIntegrationSettingRepository questIntegrationSettingRepository, QuestIntegrationSettingMapper questIntegrationSettingMapper) {
        this.questIntegrationSettingRepository = questIntegrationSettingRepository;
        this.questIntegrationSettingMapper = questIntegrationSettingMapper;
    }

    @Override
    public QuestIntegrationSettingDTO save(QuestIntegrationSettingDTO questIntegrationSettingDTO) {
        log.debug("Request to save QuestIntegrationSetting : {}", questIntegrationSettingDTO);
        QuestIntegrationSetting questIntegrationSetting = questIntegrationSettingMapper.toEntity(questIntegrationSettingDTO);
        questIntegrationSetting = questIntegrationSettingRepository.save(questIntegrationSetting);
        return questIntegrationSettingMapper.toDto(questIntegrationSetting);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuestIntegrationSettingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all QuestIntegrationSettings");
        return questIntegrationSettingRepository.findAll(pageable)
            .map(questIntegrationSettingMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<QuestIntegrationSettingDTO> findOne(Long id) {
        log.debug("Request to get QuestIntegrationSetting : {}", id);
        return questIntegrationSettingRepository.findById(id)
            .map(questIntegrationSettingMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete QuestIntegrationSetting : {}", id);
        questIntegrationSettingRepository.deleteById(id);
    }
}
