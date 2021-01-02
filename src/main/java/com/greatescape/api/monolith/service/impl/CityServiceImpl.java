package com.greatescape.api.monolith.service.impl;

import com.greatescape.api.monolith.domain.City;
import com.greatescape.api.monolith.repository.CityRepository;
import com.greatescape.api.monolith.service.CityService;
import com.greatescape.api.monolith.service.dto.CityDTO;
import com.greatescape.api.monolith.service.mapper.CityMapper;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link City}.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    private final CityMapper cityMapper;

    @Override
    public CityDTO save(CityDTO cityDTO) {
        log.debug("Request to save City : {}", cityDTO);
        City city = cityMapper.toEntity(cityDTO);
        city = cityRepository.save(city);
        return cityMapper.toDto(city);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Cities");
        return cityRepository.findAll(pageable)
            .map(cityMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<CityDTO> findOne(UUID id) {
        log.debug("Request to get City : {}", id);
        return cityRepository.findById(id)
            .map(cityMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete City : {}", id);
        cityRepository.deleteById(id);
    }
}
