package com.greatescape.api.monolith.config;

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

    private BookForm bookForm = new BookForm();

    @Data
    public static class BookForm {
        private String baseUrl;
    }
}
