package com.greatescape.api.monolith.service.impl.bookingplayerservice;

import com.greatescape.api.monolith.domain.Booking;
import com.greatescape.api.monolith.domain.enumeration.QuestIntegrationType;
import com.greatescape.api.monolith.repository.QuestIntegrationSettingRepository;
import com.greatescape.api.monolith.service.impl.bookingplayerservice.integrationsender.BookForm;
import com.greatescape.api.monolith.service.impl.bookingplayerservice.integrationsender.MirKvestov;
import com.greatescape.api.monolith.service.impl.bookingplayerservice.integrationsender.Sender;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class IntegrationSender {

    private final QuestIntegrationSettingRepository questIntegrationSettingRepository;

    private final Map<QuestIntegrationType, Sender> senderMap;

    @Configuration
    @RequiredArgsConstructor
    public static class SenderMapConfig {

        private final BookForm bookFormSender;
        private final MirKvestov mirKvestovSender;

        @Bean
        public Map<QuestIntegrationType, Sender> senderMap() {
            return Map.ofEntries(
                Map.entry(QuestIntegrationType.BOOK_FORM, bookFormSender),
                Map.entry(QuestIntegrationType.MIR_KVESTOV, mirKvestovSender)
            );
        }
    }

    public void send(Booking booking) {
        final var setting = questIntegrationSettingRepository
            .findOneByQuest(booking.getSlot().getQuest())
            .orElseThrow();

        final var sender = senderMap.get(setting.getType());

        if (sender == null) {
            throw new RuntimeException(
                String.format("Got unsupported integration type '%s'", setting.getType().toString())
            );
        }

        sender.send(setting, booking);
    }
}
