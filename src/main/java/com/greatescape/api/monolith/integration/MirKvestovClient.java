package com.greatescape.api.monolith.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.greatescape.api.monolith.config.feign.GeneralClientConfiguration;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.DatatypeConverter;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(
    name = "mirKvestovClient",
    url = "DEFINES_AT_THE_RUNTIME",
    configuration = GeneralClientConfiguration.class
)
public interface MirKvestovClient {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Slot>> getSchedule(URI uri);

    @RequestMapping(
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        method = RequestMethod.POST
    )
    ResponseEntity<BookingResponse> createBooking(
        @NotNull @Valid @RequestBody BookingRequest body,
        URI uri
    );

    @Data
    class Slot {
        private String date;
        private String time;
        @JsonProperty("is_free") private boolean isFree;
        private Integer price;
    }

    @Data
    class BookingRequest {
        @JsonProperty("first_name") private String firstName;
        @JsonProperty("family_name") private String familyName;
        private String phone;
        private String email;
        private String comment;
        private String source;
        private String date;
        private String time;
        private Integer price;
        private String md5;
    }

    @Data
    class BookingResponse {
        private boolean success;
        private String message;
    }

    @Service
    class BookingRequestSignatureBuilder {
        @SneakyThrows
        public String build(BookingRequest request, String secret) {
            final var rawSignature = request.getFirstName() +
                request.getFamilyName() +
                request.getPhone() +
                request.getEmail() +
                Optional.ofNullable(secret).orElse("");

            return DatatypeConverter.printHexBinary(
                MessageDigest.getInstance("MD5").digest(
                    rawSignature.getBytes(StandardCharsets.UTF_8)
                )
            ).toLowerCase();
        }
    }
}
