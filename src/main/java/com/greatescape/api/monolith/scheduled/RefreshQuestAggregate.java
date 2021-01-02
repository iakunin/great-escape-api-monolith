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
public class RefreshQuestAggregate implements Runnable {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * `quest_aggregation` materialized view needs to be recalculated.
     */
    @Scheduled(cron = "${app.cron.refresh-quest-aggregate}")
    @Transactional
    @Override
    public void run() {
        entityManager.createNativeQuery(
            "REFRESH MATERIALIZED VIEW quest_aggregation"
        ).executeUpdate();
    }
}
