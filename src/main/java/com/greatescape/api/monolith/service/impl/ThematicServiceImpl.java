package com.greatescape.api.monolith.service.impl;

import com.greatescape.api.monolith.domain.Thematic;
import com.greatescape.api.monolith.repository.ThematicRepository;
import com.greatescape.api.monolith.service.ThematicService;
import com.greatescape.api.monolith.service.dto.ThematicDTO;
import com.greatescape.api.monolith.service.mapper.ThematicMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Thematic}.
 */
@Service
@Transactional
public class ThematicServiceImpl implements ThematicService {

    private final Logger log = LoggerFactory.getLogger(ThematicServiceImpl.class);

    private final ThematicRepository thematicRepository;

    private final ThematicMapper thematicMapper;

    public ThematicServiceImpl(ThematicRepository thematicRepository, ThematicMapper thematicMapper) {
        this.thematicRepository = thematicRepository;
        this.thematicMapper = thematicMapper;
    }

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
    public Optional<ThematicDTO> findOne(Long id) {
        log.debug("Request to get Thematic : {}", id);
        return thematicRepository.findById(id)
            .map(thematicMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Thematic : {}", id);
        thematicRepository.deleteById(id);
    }
}
