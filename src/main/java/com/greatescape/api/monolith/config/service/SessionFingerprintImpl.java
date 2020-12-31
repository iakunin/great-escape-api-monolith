package com.greatescape.api.monolith.config.service;

import com.greatescape.api.monolith.config.feign.Properties;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

@Service
public final class SessionFingerprintImpl implements SessionFingerprint {

    private final String key;

    public SessionFingerprintImpl(final Properties properties) {
        this.key = properties.getMdcKeys().getFingerprint().getSession();
    }

    @Override
    public String get() {
        return MDC.get(this.key);
    }

    @Override
    public void set(final String fingerprint) {
        MDC.put(this.key, fingerprint);
    }

    @Override
    public void unset() {
        MDC.remove(this.key);
    }
}
