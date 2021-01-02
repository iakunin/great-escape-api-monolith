package com.greatescape.api.monolith.service.impl;

import com.greatescape.api.monolith.domain.Subscriber;
import com.greatescape.api.monolith.repository.SubscriberRepository;
import com.greatescape.api.monolith.service.SubscriberService;
import com.greatescape.api.monolith.service.dto.SubscriberDTO;
import com.greatescape.api.monolith.service.mapper.SubscriberMapper;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Subscriber}.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SubscriberServiceImpl implements SubscriberService {

    private final SubscriberRepository subscriberRepository;

    private final SubscriberMapper subscriberMapper;

    @Override
    public SubscriberDTO save(SubscriberDTO subscriberDTO) {
        log.debug("Request to save Subscriber : {}", subscriberDTO);
        Subscriber subscriber = subscriberMapper.toEntity(subscriberDTO);
        subscriber = subscriberRepository.save(subscriber);
        return subscriberMapper.toDto(subscriber);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubscriberDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Subscribers");
        return subscriberRepository.findAll(pageable)
            .map(subscriberMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<SubscriberDTO> findOne(UUID id) {
        log.debug("Request to get Subscriber : {}", id);
        return subscriberRepository.findById(id)
            .map(subscriberMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Subscriber : {}", id);
        subscriberRepository.deleteById(id);
    }
}
