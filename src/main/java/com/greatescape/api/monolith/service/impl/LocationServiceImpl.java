package com.greatescape.api.monolith.service.impl;

import com.greatescape.api.monolith.domain.Location;
import com.greatescape.api.monolith.repository.LocationRepository;
import com.greatescape.api.monolith.service.LocationService;
import com.greatescape.api.monolith.service.dto.LocationDTO;
import com.greatescape.api.monolith.service.mapper.LocationMapper;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Location}.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    private final LocationMapper locationMapper;

    @Override
    public LocationDTO save(LocationDTO locationDTO) {
        log.debug("Request to save Location : {}", locationDTO);
        Location location = locationMapper.toEntity(locationDTO);
        location = locationRepository.save(location);
        return locationMapper.toDto(location);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LocationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Locations");
        return locationRepository.findAll(pageable)
            .map(locationMapper::toDto);
    }


    public Page<LocationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return locationRepository.findAllWithEagerRelationships(pageable).map(locationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LocationDTO> findOne(UUID id) {
        log.debug("Request to get Location : {}", id);
        return locationRepository.findOneWithEagerRelationships(id)
            .map(locationMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Location : {}", id);
        locationRepository.deleteById(id);
    }
}
