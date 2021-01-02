package com.greatescape.api.monolith.scheduled;

import com.greatescape.api.monolith.domain.Booking;
import com.greatescape.api.monolith.domain.Slot;
import com.greatescape.api.monolith.repository.BookingRepository;
import java.time.Duration;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CheckMissedBooking implements Runnable {

    private final BookingRepository bookingRepository;

    /**
     * When booking is created after some time corresponding slot should be not available for
     * booking. If it's not: something's gonna wrong. We should be notified about that.
     */
    @Scheduled(cron = "${app.cron.check-missed-booking}")
    @Override
    public void run() {
        bookingRepository.findAllByCreatedAtBetween(
            ZonedDateTime.now().minus(Duration.ofMinutes(90)),
            ZonedDateTime.now().minus(Duration.ofMinutes(30))
        )
            .stream()
            .map(Booking::getSlot)
            .filter(Slot::getIsAvailable)
            .forEach(slot -> log.error(
                "Slot must be not available for booking, cause it already booked slotId='{}'",
                slot.getId().toString()
            ));
    }
}
