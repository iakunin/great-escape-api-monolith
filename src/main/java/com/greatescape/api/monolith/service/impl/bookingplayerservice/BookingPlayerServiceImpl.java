package com.greatescape.api.monolith.service.impl.bookingplayerservice;

import com.greatescape.api.monolith.repository.PlayerRepository;
import com.greatescape.api.monolith.repository.SlotRepository;
import com.greatescape.api.monolith.service.BookingPlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingPlayerServiceImpl implements BookingPlayerService {

    private final SlotRepository slotRepository;
    private final PlayerRepository playerRepository;
    private final BookingCreator bookingCreator;
    private final IntegrationSender integrationSender;

    @Override
    public CreateResponse create(CreateRequest request) {
        final var slot = slotRepository.getOne(request.getSlotId());
        final var player = playerRepository.getOne(request.getPlayerId());
        final var booking = bookingCreator.create(slot, player, request.getComment());

        integrationSender.send(booking);

        return new CreateResponse(
            booking.getId(),
            slot.getId(),
            slot.getQuest().getId(),
            player.getId()
        );
    }
}
