package com.greatescape.api.monolith.service.impl.bookingplayerservice.integrationsender;

import com.greatescape.api.monolith.domain.Booking;
import com.greatescape.api.monolith.domain.QuestIntegrationSetting;

public interface Sender {
    void send(QuestIntegrationSetting setting, Booking booking);
}
