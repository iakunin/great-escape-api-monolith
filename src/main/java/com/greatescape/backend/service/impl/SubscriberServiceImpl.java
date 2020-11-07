package com.greatescape.backend.service.impl;

import com.greatescape.backend.domain.Subscriber;
import com.greatescape.backend.repository.SubscriberRepository;
import com.greatescape.backend.service.SubscriberService;
import com.greatescape.backend.service.dto.SubscriberDTO;
import com.greatescape.backend.service.mapper.SubscriberMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Subscriber}.
 */
@Service
@Transactional
public class SubscriberServiceImpl implements SubscriberService {

    private final Logger log = LoggerFactory.getLogger(SubscriberServiceImpl.class);

    private final SubscriberRepository subscriberRepository;

    private final SubscriberMapper subscriberMapper;

    public SubscriberServiceImpl(SubscriberRepository subscriberRepository, SubscriberMapper subscriberMapper) {
        this.subscriberRepository = subscriberRepository;
        this.subscriberMapper = subscriberMapper;
    }

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
    public Optional<SubscriberDTO> findOne(Long id) {
        log.debug("Request to get Subscriber : {}", id);
        return subscriberRepository.findById(id)
            .map(subscriberMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Subscriber : {}", id);
        subscriberRepository.deleteById(id);
    }
}
