package com.greatescape.api.monolith.scheduled;

import com.greatescape.api.monolith.domain.Quest;
import com.greatescape.api.monolith.domain.QuestIntegrationSetting;
import com.greatescape.api.monolith.domain.Slot;
import com.greatescape.api.monolith.domain.enumeration.QuestIntegrationType;
import com.greatescape.api.monolith.repository.BookingRepository;
import com.greatescape.api.monolith.repository.QuestIntegrationSettingRepository;
import com.greatescape.api.monolith.repository.SlotRepository;
import com.greatescape.api.monolith.scheduled.syncslots.BookFormSchedule;
import com.greatescape.api.monolith.scheduled.syncslots.MirKvestovSchedule;
import com.greatescape.api.monolith.scheduled.syncslots.PhobiaSchedule;
import com.greatescape.api.monolith.scheduled.syncslots.Schedule;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class SyncSlots implements Runnable {

    private static final Period FETCH_PERIOD = Period.ofDays(15);

    private final QuestIntegrationSettingRepository questIntegrationSettingRepository;

    private final Map<QuestIntegrationType, Schedule> scheduleMap;

    private final Processor processor;

    @Configuration
    @RequiredArgsConstructor
    @Slf4j
    public static class ScheduleMapConfig {

        private final BookFormSchedule bookFormSchedule;
        private final MirKvestovSchedule mirKvestovSchedule;
        private final PhobiaSchedule phobiaSchedule;

        @Bean
        public Map<QuestIntegrationType, Schedule> scheduleMap() {
            return Map.ofEntries(
                Map.entry(QuestIntegrationType.BOOK_FORM, bookFormSchedule),
                Map.entry(QuestIntegrationType.MIR_KVESTOV, mirKvestovSchedule),
                Map.entry(QuestIntegrationType.PHOBIA, phobiaSchedule)
            );
        }
    }

    @Scheduled(cron = "${app.cron.sync-slots}")
    @Override
    public void run() {
        questIntegrationSettingRepository
            .findAll()
            .forEach(setting -> {
                try {
                    processor.process(this.getSchedule(setting), setting.getQuest());
                } catch (Exception e) {
                    log.error(
                        "Exception occurred during processing questId={}",
                        setting.getQuest().getId(),
                        e
                    );
                }
            });
    }

    private Collection<Slot> getSchedule(QuestIntegrationSetting setting) {
        final var schedule = scheduleMap.get(setting.getType());

        if (schedule == null) {
            throw new RuntimeException(
                String.format("Got unsupported integration type '%s'", setting.getType().toString())
            );
        }

        return schedule.getSchedule(setting, FETCH_PERIOD);
    }

    @Slf4j
    @RequiredArgsConstructor
    @Service
    public static class Processor {

        private final SlotRepository slotRepository;

        private final BookingRepository bookingRepository;

        @Transactional(propagation = Propagation.REQUIRES_NEW)
        public void process(Collection<Slot> schedule, Quest quest) {
            this.processSlots(schedule, quest);
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
    }
}
