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
     * quest_aggregation materialized view needed to be recalculated.
     *
     * This is scheduled to get fired every 10 minutes.
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    @Transactional
    @Override
    public void run() {
        entityManager.createNativeQuery(
            "REFRESH MATERIALIZED VIEW quest_aggregation"
        ).executeUpdate();
    }
}
