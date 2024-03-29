package com.greatescape.api.monolith.service.impl;

import com.greatescape.api.monolith.domain.Slot;
import com.greatescape.api.monolith.repository.SlotRepository;
import com.greatescape.api.monolith.service.SlotService;
import com.greatescape.api.monolith.service.dto.SlotDTO;
import com.greatescape.api.monolith.service.mapper.SlotMapper;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Slot}.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SlotServiceImpl implements SlotService {

    private final SlotRepository slotRepository;

    private final SlotMapper slotMapper;

    @Override
    public SlotDTO save(SlotDTO slotDTO) {
        log.debug("Request to save Slot : {}", slotDTO);
        Slot slot = slotMapper.toEntity(slotDTO);
        slot = slotRepository.save(slot);
        return slotMapper.toDto(slot);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SlotDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Slots");
        return slotRepository.findAll(pageable)
            .map(slotMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<SlotDTO> findOne(UUID id) {
        log.debug("Request to get Slot : {}", id);
        return slotRepository.findById(id)
            .map(slotMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Slot : {}", id);
        slotRepository.deleteById(id);
    }
}
