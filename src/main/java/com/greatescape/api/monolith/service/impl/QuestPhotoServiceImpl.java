package com.greatescape.api.monolith.service.impl;

import com.greatescape.api.monolith.domain.QuestPhoto;
import com.greatescape.api.monolith.repository.QuestPhotoRepository;
import com.greatescape.api.monolith.service.QuestPhotoService;
import com.greatescape.api.monolith.service.dto.QuestPhotoDTO;
import com.greatescape.api.monolith.service.mapper.QuestPhotoMapper;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link QuestPhoto}.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class QuestPhotoServiceImpl implements QuestPhotoService {

    private final QuestPhotoRepository questPhotoRepository;

    private final QuestPhotoMapper questPhotoMapper;

    @Override
    public QuestPhotoDTO save(QuestPhotoDTO questPhotoDTO) {
        log.debug("Request to save QuestPhoto : {}", questPhotoDTO);
        QuestPhoto questPhoto = questPhotoMapper.toEntity(questPhotoDTO);
        questPhoto = questPhotoRepository.save(questPhoto);
        return questPhotoMapper.toDto(questPhoto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuestPhotoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all QuestPhotos");
        return questPhotoRepository.findAll(pageable)
            .map(questPhotoMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<QuestPhotoDTO> findOne(UUID id) {
        log.debug("Request to get QuestPhoto : {}", id);
        return questPhotoRepository.findById(id)
            .map(questPhotoMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete QuestPhoto : {}", id);
        questPhotoRepository.deleteById(id);
    }
}
