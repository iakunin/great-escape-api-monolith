package com.greatescape.api.monolith.scheduled.syncslots;

import com.greatescape.api.monolith.domain.QuestIntegrationSetting;
import com.greatescape.api.monolith.domain.Slot;
import com.greatescape.api.monolith.integration.PhobiaClient;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public final class PhobiaSchedule implements Schedule {

    private final PhobiaClient client;

    @Override
    public Collection<Slot> getSchedule(QuestIntegrationSetting setting, Period fetchPeriod) {
        final var timezone = setting.getQuest().getLocation().getCity().getTimezone();

        return Objects.requireNonNull(
            this.client.getSchedule(
                LocalDate.now(timezone),
                LocalDate.now(timezone).plus(fetchPeriod),
                ((QuestIntegrationSetting.Phobia) setting.getSettings()).getQuestId()
            ).getBody()
        ).stream()
            .flatMap(item -> item.getSlotList().stream())
            .map(slot -> new Slot()
                .setIsAvailable(slot.isAvailable())
                .setPrice(slot.getPrice())
                .setDateTimeLocal(
                    DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                        .withZone(ZoneId.of("UTC"))
                        .parse(
                            String.format("%s %s", slot.getDate(), slot.getTime()),
                            Instant::from
                        )
                )
            ).collect(Collectors.toUnmodifiableList());
    }
}
