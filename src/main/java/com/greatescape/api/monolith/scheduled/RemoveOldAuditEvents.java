package com.greatescape.api.monolith.scheduled;

import com.greatescape.api.monolith.repository.PersistenceAuditEventRepository;
import io.github.jhipster.config.JHipsterProperties;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RemoveOldAuditEvents implements Runnable {

    private final PersistenceAuditEventRepository persistenceAuditEventRepository;

    private final JHipsterProperties jHipsterProperties;

    /**
     * Old audit events should be automatically deleted after 30 days.
     */
    @Scheduled(cron = "${app.cron.remove-old-audit-events}")
    @Override
    public void run() {
        persistenceAuditEventRepository
            .findByAuditEventDateBefore(Instant.now().minus(jHipsterProperties.getAuditEvents().getRetentionPeriod(), ChronoUnit.DAYS))
            .forEach(auditEvent -> {
                log.debug("Deleting audit data {}", auditEvent);
                persistenceAuditEventRepository.delete(auditEvent);
            });
    }
}
