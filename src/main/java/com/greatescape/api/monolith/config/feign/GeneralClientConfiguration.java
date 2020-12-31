package com.greatescape.api.monolith.config.feign;

import com.greatescape.api.monolith.config.service.SessionFingerprint;
import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeneralClientConfiguration {
    @Bean
    public RequestInterceptor sessionFingerprintInterceptor(
        final Properties properties,
        final SessionFingerprint fingerprint
    ) {
        return new SessionFingerprintInterceptor(
            properties.getSessionFingerprintHeaderName(),
            fingerprint
        );
    }

    @Bean
    public Logger.Level level() {
        return Logger.Level.FULL;
    }

    @Bean
    public Logger logger() {
        return new Slf4jFeignLogger();
    }
}
