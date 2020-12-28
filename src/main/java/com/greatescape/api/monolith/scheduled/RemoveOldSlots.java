package com.greatescape.api.monolith.scheduled;

import com.greatescape.api.monolith.domain.City;
import com.greatescape.api.monolith.repository.BookingRepository;
import com.greatescape.api.monolith.repository.CityRepository;
import com.greatescape.api.monolith.repository.LocationRepository;
import com.greatescape.api.monolith.repository.QuestRepository;
import com.greatescape.api.monolith.repository.SlotRepository;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RemoveOldSlots implements Runnable {

    private final CityRepository cityRepository;

    private final LocationRepository locationRepository;

    private final QuestRepository questRepository;

    private final SlotRepository slotRepository;

    private final BookingRepository bookingRepository;

    /**
     * Old slots should be automatically deleted.
     *
     * This is scheduled to get fired every hour in 15 minutes.
     */
    //@TODO: uncomment me before deploying to production
    //@Scheduled(cron = "0 15 * * * ?")
    @Override
    public void run() {
        for (City city: cityRepository.findAll()) {
            final ZonedDateTime beginOfTheDay = ZonedDateTime
                .now(city.getTimezone())
                .truncatedTo(ChronoUnit.DAYS);

            locationRepository.findAllByCity(city)
                .stream()
                .flatMap(location -> questRepository.findAllByLocation(location).stream())
                .flatMap(quest -> slotRepository.findAllByQuest(quest).stream())
                .filter(slot -> slot.getDateTimeWithTimeZone().isBefore(beginOfTheDay))
                .filter(slot -> !bookingRepository.existsBySlot(slot))
                .forEach(slot -> {
                    log.debug("Deleting slot '{}'", slot);
                    try {
                        slotRepository.delete(slot);
                    } catch (Exception e) {
                        log.error("Unable to delete slot '{}'", slot, e);
                    }
                });
        }
    }
}
