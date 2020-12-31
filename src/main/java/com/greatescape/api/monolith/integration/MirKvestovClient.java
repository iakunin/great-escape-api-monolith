package com.greatescape.api.monolith.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.greatescape.api.monolith.config.feign.GeneralClientConfiguration;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
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

    @GetMapping(produces = "application/json")
    ResponseEntity<List<Slot>> getSchedule(URI uri);

    @RequestMapping(
        produces = "application/json",
        consumes = "application/json",
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
        private String md5;
        private String date;
        private String time;
        private Integer price;
    }

    @Data
    class BookingResponse {
        private boolean success;
        private String message;
    }
}
