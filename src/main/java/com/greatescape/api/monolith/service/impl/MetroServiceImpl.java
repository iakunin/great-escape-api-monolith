package com.greatescape.api.monolith.service.impl;

import com.greatescape.api.monolith.domain.Metro;
import com.greatescape.api.monolith.repository.MetroRepository;
import com.greatescape.api.monolith.service.MetroService;
import com.greatescape.api.monolith.service.dto.MetroDTO;
import com.greatescape.api.monolith.service.mapper.MetroMapper;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Metro}.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MetroServiceImpl implements MetroService {

    private final MetroRepository metroRepository;

    private final MetroMapper metroMapper;

    @Override
    public MetroDTO save(MetroDTO metroDTO) {
        log.debug("Request to save Metro : {}", metroDTO);
        Metro metro = metroMapper.toEntity(metroDTO);
        metro = metroRepository.save(metro);
        return metroMapper.toDto(metro);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MetroDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Metros");
        return metroRepository.findAll(pageable)
            .map(metroMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<MetroDTO> findOne(UUID id) {
        log.debug("Request to get Metro : {}", id);
        return metroRepository.findById(id)
            .map(metroMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Metro : {}", id);
        metroRepository.deleteById(id);
    }
}
