package com.greatescape.api.monolith.web.rest;

import com.greatescape.api.monolith.service.MailService;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FeedbackController {

    private final MailService mailService;

    @PostMapping("/feedback")
    public ResponseEntity<?> create(@Valid @RequestBody Feedback request) {
        this.mailService.sendAnonymousFeedbackEmail(
            request.getName(),
            request.getEmail(),
            request.getText()
        );

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Value
    public static class Feedback {

        @NotNull
        @Size(min = 1, max = 100)
        String name;

        @NotNull
        @Email
        @Size(min = 5, max = 254)
        String email;

        @NotNull
        @Size(min = 1)
        String text;
    }
}
