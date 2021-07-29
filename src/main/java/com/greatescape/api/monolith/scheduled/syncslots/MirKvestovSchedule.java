package com.greatescape.api.monolith.scheduled.syncslots;

import com.greatescape.api.monolith.domain.QuestIntegrationSetting;
import com.greatescape.api.monolith.domain.Slot;
import com.greatescape.api.monolith.integration.MirKvestovClient;
import java.time.Instant;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public final class MirKvestovSchedule implements Schedule {

    private static final String TIME_PATTERN = "HH:mm";

    private final MirKvestovClient client;

    @Override
    public Collection<Slot> getSchedule(QuestIntegrationSetting setting, Period fetchPeriod) {
        return Objects.requireNonNull(
            client.getSchedule(
                ((QuestIntegrationSetting.MirKvestov) setting.getSettings()).getScheduleUrl()
            ).getBody()
        ).stream()
            .map(slot -> new Slot()
                .setIsAvailable((Boolean) slot.get("is_free"))
                .setPrice(
                    Double.valueOf(slot.get("price").toString()).intValue()
                )
                .setExternalState(this.buildExternalState(slot))
                .setDateTimeLocal(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd " + TIME_PATTERN)
                        .withZone(ZoneId.of("UTC"))
                        .parse(
                            String.format(
                                "%s %s",
                                slot.get("date"),
                                slot.get("time").toString().substring(0, TIME_PATTERN.length())
                            ),
                            Instant::from
                        )
                )
            ).collect(
                Collectors.toMap(
                    Slot::getDateTimeLocal,
                    Function.identity(),
                    (first, second) -> second
                )
            ).values();
    }

    private Map<String, Object> buildExternalState(Map<String, Object> slot) {
        final var result = new HashMap<>(slot);
        result.remove("date");
        result.remove("time");
        result.remove("price");
        result.remove("is_free");

        return result;
    }
}
