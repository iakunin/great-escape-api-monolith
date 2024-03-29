package com.greatescape.api.monolith.scheduled;

import com.greatescape.api.monolith.ApiMonolithApp;
import com.greatescape.api.monolith.domain.PersistentAuditEvent;
import com.greatescape.api.monolith.repository.PersistenceAuditEventRepository;
import com.greatescape.api.monolith.service.AuditEventService;
import io.github.jhipster.config.JHipsterProperties;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for {@link AuditEventService}.
 */
@SpringBootTest(classes = ApiMonolithApp.class)
@Transactional
public class RemoveOldAuditEventsIT {
    @Autowired
    private RemoveOldAuditEvents removeOldAuditEvents;

    @Autowired
    private PersistenceAuditEventRepository persistenceAuditEventRepository;

    @Autowired
    private JHipsterProperties jHipsterProperties;

    private PersistentAuditEvent auditEventOld;

    private PersistentAuditEvent auditEventWithinRetention;

    private PersistentAuditEvent auditEventNew;

    @BeforeEach
    public void init() {
        auditEventOld = new PersistentAuditEvent();
        auditEventOld.setAuditEventDate(
            Instant.now().minus(
                jHipsterProperties.getAuditEvents().getRetentionPeriod() + 1,
                ChronoUnit.DAYS
            )
        );
        auditEventOld.setPrincipal("test-user-old");
        auditEventOld.setAuditEventType("test-type");

        auditEventWithinRetention = new PersistentAuditEvent();
        auditEventWithinRetention.setAuditEventDate(
            Instant.now().minus(
                jHipsterProperties.getAuditEvents().getRetentionPeriod() - 1,
                ChronoUnit.DAYS
            )
        );
        auditEventWithinRetention.setPrincipal("test-user-retention");
        auditEventWithinRetention.setAuditEventType("test-type");

        auditEventNew = new PersistentAuditEvent();
        auditEventNew.setAuditEventDate(Instant.now());
        auditEventNew.setPrincipal("test-user-new");
        auditEventNew.setAuditEventType("test-type");
    }

    @Test
    @Transactional
    public void verifyOldAuditEventsAreDeleted() {
        persistenceAuditEventRepository.deleteAll();
        persistenceAuditEventRepository.save(auditEventOld);
        persistenceAuditEventRepository.save(auditEventWithinRetention);
        persistenceAuditEventRepository.save(auditEventNew);

        persistenceAuditEventRepository.flush();
        removeOldAuditEvents.run();
        persistenceAuditEventRepository.flush();

        assertThat(persistenceAuditEventRepository.findAll().size()).isEqualTo(2);
        Assertions.assertThat(persistenceAuditEventRepository.findByPrincipal("test-user-old")).isEmpty();
        Assertions.assertThat(persistenceAuditEventRepository.findByPrincipal("test-user-retention")).isNotEmpty();
        Assertions.assertThat(persistenceAuditEventRepository.findByPrincipal("test-user-new")).isNotEmpty();
    }
}
