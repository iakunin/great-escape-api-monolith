package com.greatescape.api.monolith.scheduled;

import com.greatescape.api.monolith.domain.Quest;
import com.greatescape.api.monolith.domain.QuestIntegrationSetting;
import com.greatescape.api.monolith.domain.Slot;
import com.greatescape.api.monolith.domain.enumeration.QuestIntegrationType;
import com.greatescape.api.monolith.integration.BookFormClient;
import com.greatescape.api.monolith.integration.MirKvestovClient;
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

    private static final Period FETCH_PERIOD = Period.ofDays(60);

    private final QuestIntegrationSettingRepository questIntegrationSettingRepository;

    private final Processor processor;

    private final BookFormSchedule bookFormSchedule;
    private final MirKvestovSchedule mirKvestovSchedule;

    @Scheduled(cron = "${app.cron.sync-slots}")
    @Override
    public void run() {
        questIntegrationSettingRepository
            .findAll()
            .forEach(setting -> processor.process(
                this.getSchedule(setting),
                setting.getQuest()
            ));
    }

    private Collection<Slot> getSchedule(QuestIntegrationSetting setting) {
        if (setting.getType() == QuestIntegrationType.BOOK_FORM) {
            return bookFormSchedule.getSchedule(setting, FETCH_PERIOD);
        } else if (setting.getType() == QuestIntegrationType.MIR_KVESTOV) {
            return mirKvestovSchedule.getSchedule(setting, FETCH_PERIOD);
        } else {
            throw new RuntimeException(
                String.format("Got unsupported integration type '%s'", setting.getType().toString())
            );
        }
    }

    interface Schedule {
        Collection<Slot> getSchedule(QuestIntegrationSetting setting, Period fetchPeriod);
    }

    @Slf4j
    @RequiredArgsConstructor
    @Service
    public static final class BookFormSchedule implements Schedule {

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

    @Slf4j
    @RequiredArgsConstructor
    @Service
    public static final class MirKvestovSchedule implements Schedule {

        private final MirKvestovClient client;

        @Override
        public Collection<Slot> getSchedule(QuestIntegrationSetting setting, Period fetchPeriod) {
            return Objects.requireNonNull(
                client.getSchedule(
                    ((QuestIntegrationSetting.MirKvestov) setting.getSettings()).getScheduleUrl()
                ).getBody()
            ).stream()
            .map(slot -> new Slot()
                .setIsAvailable(slot.isFree())
                .setPrice(slot.getPrice())
                .setDateTimeLocal(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                        .withZone(ZoneId.of("UTC"))
                        .parse(
                            String.format("%s %s", slot.getDate(), slot.getTime()),
                            Instant::from
                        )
                )
            ).collect(Collectors.toUnmodifiableList());
        }
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
