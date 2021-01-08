package com.greatescape.api.monolith.service.sms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
@Slf4j
public class StubSender implements Sender {
    @Override
    public void send(Request request) {
        log.info(
            "Calling Stub sms-sender for phone='{}' with text='{}'",
            request.getPhone(),
            request.getText()
        );
    }
}
