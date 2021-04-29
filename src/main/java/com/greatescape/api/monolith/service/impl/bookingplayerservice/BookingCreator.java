package com.greatescape.api.monolith.service.impl.bookingplayerservice;

import com.greatescape.api.monolith.domain.Booking;
import com.greatescape.api.monolith.domain.Player;
import com.greatescape.api.monolith.domain.Slot;
import com.greatescape.api.monolith.domain.enumeration.BookingStatus;
import com.greatescape.api.monolith.repository.BookingRepository;
import com.greatescape.api.monolith.repository.SlotAggregationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookingCreator {

    private final SlotAggregationRepository slotAggregationRepository;
    private final BookingRepository bookingRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Booking create(Slot slot, Player player, String comment) {
        final var slotAggregation = slotAggregationRepository.getOne(slot.getId());
        return bookingRepository.save(
            new Booking()
                .setStatus(BookingStatus.NEW)
                .setPrice(slot.getPrice())
                .setDiscountInPercents(slotAggregation.getDiscountInPercents())
                .setCommissionInPercents(slotAggregation.getCommissionInPercents())
                .setComment(comment)
                .setSlot(slot)
                .setQuest(slot.getQuest())
                .setPlayer(player)
        );
    }
}
