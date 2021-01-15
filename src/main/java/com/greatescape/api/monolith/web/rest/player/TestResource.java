package com.greatescape.api.monolith.web.rest.player;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
    private String secret;

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return new ResponseEntity<>(secret, HttpStatus.OK);
    }
}
