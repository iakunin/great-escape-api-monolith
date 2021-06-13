package com.greatescape.api.monolith.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "google.recaptcha", ignoreUnknownFields = false)
@Data
public class GoogleReCaptchaProperties {
    private String baseUrl;
    private String secretKey;
    private float threshold;
}
