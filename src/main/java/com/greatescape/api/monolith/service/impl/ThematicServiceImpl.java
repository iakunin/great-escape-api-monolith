package com.greatescape.api.monolith.service.impl;

import com.greatescape.api.monolith.domain.Thematic;
import com.greatescape.api.monolith.repository.ThematicRepository;
import com.greatescape.api.monolith.service.ThematicService;
import com.greatescape.api.monolith.service.dto.ThematicDTO;
import com.greatescape.api.monolith.service.mapper.ThematicMapper;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Thematic}.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ThematicServiceImpl implements ThematicService {

    private final ThematicRepository thematicRepository;

    private final ThematicMapper thematicMapper;

    @Override
    public ThematicDTO save(ThematicDTO thematicDTO) {
        log.debug("Request to save Thematic : {}", thematicDTO);
        Thematic thematic = thematicMapper.toEntity(thematicDTO);
        thematic = thematicRepository.save(thematic);
        return thematicMapper.toDto(thematic);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ThematicDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Thematics");
        return thematicRepository.findAll(pageable)
            .map(thematicMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<ThematicDTO> findOne(UUID id) {
        log.debug("Request to get Thematic : {}", id);
        return thematicRepository.findById(id)
            .map(thematicMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Thematic : {}", id);
        thematicRepository.deleteById(id);
    }
}
