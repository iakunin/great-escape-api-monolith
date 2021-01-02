package com.greatescape.api.monolith.service.impl;

import com.greatescape.api.monolith.domain.Player;
import com.greatescape.api.monolith.repository.PlayerRepository;
import com.greatescape.api.monolith.service.PlayerAdminService;
import com.greatescape.api.monolith.service.dto.PlayerDTO;
import com.greatescape.api.monolith.service.mapper.PlayerMapper;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Player}.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PlayerAdminServiceImpl implements PlayerAdminService {

    private final PlayerRepository playerRepository;

    private final PlayerMapper playerMapper;

    @Override
    public PlayerDTO save(PlayerDTO playerDTO) {
        log.debug("Request to save Player : {}", playerDTO);
        Player player = playerMapper.toEntity(playerDTO);
        player = playerRepository.save(player);
        return playerMapper.toDto(player);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PlayerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Players");
        return playerRepository.findAll(pageable)
            .map(playerMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<PlayerDTO> findOne(UUID id) {
        log.debug("Request to get Player : {}", id);
        return playerRepository.findById(id)
            .map(playerMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Player : {}", id);
        playerRepository.deleteById(id);
    }
}
