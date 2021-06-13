package com.greatescape.api.monolith.service.recaptcha;

import com.greatescape.api.monolith.config.GoogleReCaptchaProperties;
import com.greatescape.api.monolith.integration.GoogleReCaptchaClient;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReCaptchaImpl implements ReCaptcha {

    private static final Pattern TOKEN_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");

    private final GoogleReCaptchaProperties properties;

    private final GoogleReCaptchaClient client;

    @Override
    public void validate(String token) throws InvalidReCaptchaException {
        if (!this.isTokenValid(token)) {
            throw new InvalidReCaptchaException("Token contains invalid characters");
        }

        final var response = this.client.verify(this.properties.getSecretKey(), token).getBody();
        if (response == null) {
            throw new InvalidReCaptchaException("Null response from GoogleReCaptchaClient");
        }

        if (
            !response.isSuccess()
                ||
            response.getScore() < this.properties.getThreshold()
        ) {
            throw new InvalidReCaptchaException("reCaptcha was not successfully validated");
        }
    }

    private boolean isTokenValid(String token) {
        return StringUtils.hasLength(token) && TOKEN_PATTERN.matcher(token).matches();
    }
}
