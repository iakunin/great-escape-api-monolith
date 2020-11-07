package com.greatescape.backend.service.impl;

import com.greatescape.backend.domain.Metro;
import com.greatescape.backend.repository.MetroRepository;
import com.greatescape.backend.service.MetroService;
import com.greatescape.backend.service.dto.MetroDTO;
import com.greatescape.backend.service.mapper.MetroMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Metro}.
 */
@Service
@Transactional
public class MetroServiceImpl implements MetroService {

    private final Logger log = LoggerFactory.getLogger(MetroServiceImpl.class);

    private final MetroRepository metroRepository;

    private final MetroMapper metroMapper;

    public MetroServiceImpl(MetroRepository metroRepository, MetroMapper metroMapper) {
        this.metroRepository = metroRepository;
        this.metroMapper = metroMapper;
    }

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
    public Optional<MetroDTO> findOne(Long id) {
        log.debug("Request to get Metro : {}", id);
        return metroRepository.findById(id)
            .map(metroMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Metro : {}", id);
        metroRepository.deleteById(id);
    }
}
