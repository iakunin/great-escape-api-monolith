package com.greatescape.api.monolith.scheduled.syncslots;

import com.greatescape.api.monolith.domain.QuestIntegrationSetting;
import com.greatescape.api.monolith.domain.Slot;
import java.time.Period;
import java.util.Collection;

public interface Schedule {
    Collection<Slot> getSchedule(QuestIntegrationSetting setting, Period fetchPeriod);
}
