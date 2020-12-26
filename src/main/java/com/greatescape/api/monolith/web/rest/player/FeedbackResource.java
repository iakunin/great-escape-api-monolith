package com.greatescape.api.monolith.web.rest.player;

import com.greatescape.api.monolith.service.MailService;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/player-api")
@RequiredArgsConstructor
public class FeedbackResource {

    private final MailService mailService;

    @PostMapping("/feedback")
    public ResponseEntity<?> create(@Valid @RequestBody Request request) {
        this.mailService.sendAnonymousFeedbackEmail(
            request.getName(),
            request.getEmail(),
            request.getText()
        );

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Value
    public static class Request {

        @NotBlank
        String name;

        @NotBlank
        @Email
        String email;

        @NotBlank
        String text;
    }
}
