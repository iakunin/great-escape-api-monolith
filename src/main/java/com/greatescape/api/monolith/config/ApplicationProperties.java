package com.greatescape.api.monolith.config;

import java.time.Duration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to ApiMonolith.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
@Data
public class ApplicationProperties {

    private Integration integration = new Integration();
    private Slot slot = new Slot();
    private Scheduled cron = new Scheduled();
    private Email email = new Email();
    private String cloudSecretTest = "";

    @Data
    public static class Integration {
        private BookForm bookForm = new BookForm();
        private Phobia phobia = new Phobia();
        private SmsRu smsRu = new SmsRu();

        @Data
        public static class BookForm {
            private String baseUrl;
        }

        @Data
        public static class Phobia {
            private String baseUrl;
        }

        @Data
        public static class SmsRu {
            private String apiId;
        }
    }

    @Data
    public static class Slot {
        private Duration availabilityDelta;
    }

    @Data
    public static class Scheduled {
        private String checkMissedBooking;
        private String refreshQuestAggregate;
        private String refreshSlotAggregate;
        private String removeNotActivatedUsers;
        private String removeOldAuditEvents;
        private String syncSlots;
    }

    @Data
    public static class Email {
        private String info;
        private String siteFeedbackForm;
    }
}
