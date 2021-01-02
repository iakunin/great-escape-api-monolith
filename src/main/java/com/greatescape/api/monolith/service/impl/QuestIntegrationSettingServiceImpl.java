package com.greatescape.api.monolith.service.impl;

import com.greatescape.api.monolith.domain.QuestIntegrationSetting;
import com.greatescape.api.monolith.repository.QuestIntegrationSettingRepository;
import com.greatescape.api.monolith.service.QuestIntegrationSettingService;
import com.greatescape.api.monolith.service.dto.QuestIntegrationSettingDTO;
import com.greatescape.api.monolith.service.mapper.QuestIntegrationSettingMapper;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link QuestIntegrationSetting}.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class QuestIntegrationSettingServiceImpl implements QuestIntegrationSettingService {

    private final QuestIntegrationSettingRepository questIntegrationSettingRepository;

    private final QuestIntegrationSettingMapper questIntegrationSettingMapper;

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
    public Optional<QuestIntegrationSettingDTO> findOne(UUID id) {
        log.debug("Request to get QuestIntegrationSetting : {}", id);
        return questIntegrationSettingRepository.findById(id)
            .map(questIntegrationSettingMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete QuestIntegrationSetting : {}", id);
        questIntegrationSettingRepository.deleteById(id);
    }
}
