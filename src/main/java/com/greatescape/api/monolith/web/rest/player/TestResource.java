package com.greatescape.api.monolith.web.rest.player;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.secretmanager.SecretManagerTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/player-api")
@RequiredArgsConstructor
@Slf4j
public class TestResource {

    @Value("${sm://test}")
    private final String secret;

    private final SecretManagerTemplate template;

    @GetMapping("/test")
    public ResponseEntity<?> test() {

        return new ResponseEntity<>(
            Map.of(
                "fromTemplate", template.getSecretString("test"),
                "fromValue", secret
            ),
            HttpStatus.OK
        );
    }
}
