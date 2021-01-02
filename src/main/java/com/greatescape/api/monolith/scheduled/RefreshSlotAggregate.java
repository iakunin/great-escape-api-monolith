package com.greatescape.api.monolith.scheduled;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class RefreshSlotAggregate implements Runnable {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * `slot_aggregation` materialized view needs to be recalculated.
     */
    @Scheduled(cron = "${app.cron.refresh-slot-aggregate}")
    @Transactional
    @Override
    public void run() {
        entityManager.createNativeQuery(
            "REFRESH MATERIALIZED VIEW slot_aggregation"
        ).executeUpdate();
    }
}
