package com.greatescape.api.monolith.service.impl;

import com.greatescape.api.monolith.domain.Player;
import com.greatescape.api.monolith.repository.PlayerRepository;
import com.greatescape.api.monolith.service.PlayerPlayerService;
import com.greatescape.api.monolith.service.UserService;
import com.greatescape.api.monolith.service.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlayerPlayerServiceImpl implements PlayerPlayerService {

    private final UserService userService;
    private final PlayerRepository playerRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public CreateResponse create(CreateRequest request) {
        return new CreateResponse(
            playerRepository.save(
                new Player()
                    .setName(request.getName())
                    .setPhone(request.getPhone())
                    .setEmail(request.getEmail())
                    .setInternalUser(userService.createUser(
                        new UserDTO()
                            .setLogin(request.getPhone())
                            .setEmail(request.getEmail())
                            .setFirstName(request.getName())
                            .setLastName(".")
                    ))
            ).getId()
        );
    }
}
