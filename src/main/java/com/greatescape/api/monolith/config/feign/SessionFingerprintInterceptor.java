package com.greatescape.api.monolith.config.feign;

import com.greatescape.api.monolith.config.service.SessionFingerprint;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
final class SessionFingerprintInterceptor implements RequestInterceptor {

    private final String header;

    private final SessionFingerprint fingerprint;

    @Override
    public void apply(final RequestTemplate template) {
        final Map<String, List<String>> headers = new ConcurrentHashMap<>();

        headers.put(
            this.header,
            Collections.singletonList(
                this.fingerprint.get()
            )
        );

        headers.forEach(template::header);
    }
}
