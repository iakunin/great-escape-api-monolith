package com.greatescape.api.monolith.scheduled.syncslots;

import com.greatescape.api.monolith.domain.QuestIntegrationSetting;
import com.greatescape.api.monolith.domain.Slot;
import com.greatescape.api.monolith.integration.BookFormClient;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public final class BookFormSchedule implements Schedule {

    private final BookFormClient bookFormClient;

    @Override
    public Collection<Slot> getSchedule(QuestIntegrationSetting setting, Period fetchPeriod) {
        final var timezone = setting.getQuest().getLocation().getCity().getTimezone();

        return this.flattenSchedule(
            Objects.requireNonNull(bookFormClient.getSchedule(
                ((QuestIntegrationSetting.BookForm) setting.getSettings()).getServiceId(),
                LocalDate.now(timezone),
                LocalDate.now(timezone).plus(fetchPeriod)
            ).getBody())
        );
    }

    private Collection<Slot> flattenSchedule(
        Map<String, Map<String, Map<String, BookFormClient.Slot>>> schedule
    ) {
        Collection<Slot> result = new LinkedList<>();

        schedule.values().forEach(
            item -> item.forEach(
                (date, map) -> map.forEach(
                    (time, slot) -> result.add(
                        new Slot()
                            .setExternalId(slot.getId())
                            .setIsAvailable(slot.isFree())
                            .setPrice(slot.getPrice())
                            .setDateTimeLocal(
                                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                                    .withZone(ZoneId.of("UTC"))
                                    .parse(
                                        String.format("%s %s", date, time),
                                        Instant::from
                                    )
                            )
                    )
                )
            )
        );

        return result;
    }
}
