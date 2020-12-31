package com.greatescape.api.monolith.scheduled;

import com.greatescape.api.monolith.domain.Quest;
import com.greatescape.api.monolith.domain.QuestIntegrationSetting;
import com.greatescape.api.monolith.domain.Slot;
import com.greatescape.api.monolith.domain.enumeration.QuestIntegrationType;
import com.greatescape.api.monolith.integration.bookform.Client;
import com.greatescape.api.monolith.repository.BookingRepository;
import com.greatescape.api.monolith.repository.QuestIntegrationSettingRepository;
import com.greatescape.api.monolith.repository.SlotRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class SyncSlots implements Runnable {

    private final QuestIntegrationSettingRepository questIntegrationSettingRepository;

    private final Runner runner;

    @Scheduled(cron = "0 */5 * * * ?")
    @Override
    public void run() {
        questIntegrationSettingRepository
            .findAllByType(QuestIntegrationType.BOOK_FORM)
            .forEach(runner::run);
    }

    @Slf4j
    @RequiredArgsConstructor
    @Service
    public static class Runner {

        private static final Period FETCH_PERIOD = Period.ofDays(60);

        private final SlotRepository slotRepository;

        private final BookingRepository bookingRepository;

        private final Client bookFormClient;

        @Transactional(propagation = Propagation.REQUIRES_NEW)
        public void run(QuestIntegrationSetting setting) {
            final var timezone = setting.getQuest().getLocation().getCity().getTimezone();

            final var schedule = bookFormClient.getSchedule(
                ((QuestIntegrationSetting.BookForm) setting.getSettings()).getServiceId(),
                LocalDate.now(timezone),
                LocalDate.now(timezone).plus(FETCH_PERIOD)
            ).getBody();

            final var flattened = this.flattenSchedule(Objects.requireNonNull(schedule));

            this.processSlots(flattened, setting.getQuest());
        }

        private void processSlots(Collection<Slot> remote, Quest quest) {
            final var local = slotRepository.findAllByQuest(quest).stream()
                .collect(Collectors.toMap(
                    Slot::getDateTimeLocal,
                    Function.identity()
                ));

            this.deleteAbsentInRemote(local, remote);

            remote.forEach(remoteSlot -> {
                final var localSlot = local.get(remoteSlot.getDateTimeLocal());
                if (localSlot == null) {
                    slotRepository.save(
                        remoteSlot
                            .setQuest(quest)
                            .setDateTimeWithTimeZone(
                                ZonedDateTime.of(
                                    LocalDateTime.ofInstant(
                                        remoteSlot.getDateTimeLocal(),
                                        ZoneId.of("UTC")
                                    ),
                                    quest.getLocation().getCity().getTimezone()
                                )
                            )
                    );
                } else {
                    slotRepository.save(
                        localSlot
                            .setExternalId(remoteSlot.getExternalId())
                            .setIsAvailable(remoteSlot.getIsAvailable())
                            .setPrice(remoteSlot.getPrice())
                    );
                }
            });
        }

        private void deleteAbsentInRemote(Map<Instant, Slot> local, Collection<Slot> remote) {
            final var toDelete = new HashMap<>(local);
            remote.stream().map(Slot::getDateTimeLocal).forEach(toDelete::remove);
            toDelete.values().stream()
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

        private Collection<Slot> flattenSchedule(
            Map<String, Map<String, Map<String, Client.Slot>>> schedule
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
}
