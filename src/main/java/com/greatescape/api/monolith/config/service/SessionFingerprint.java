package com.greatescape.api.monolith.config.service;

public interface SessionFingerprint {
    String get();

    void set(String fingerprint);

    void unset();
}
