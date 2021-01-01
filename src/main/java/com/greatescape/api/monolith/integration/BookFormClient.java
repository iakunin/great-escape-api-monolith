package com.greatescape.api.monolith.integration;

import com.greatescape.api.monolith.config.feign.GeneralClientConfiguration;
import java.time.LocalDate;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "bookFormClient",
    url = "${app.book-form.base-url}/api/v1",
    configuration = GeneralClientConfiguration.class
)
public interface BookFormClient {

    @GetMapping(value = "/schedule", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<
        Map<String, // service_id (64D2D5581BBA11EB82AF5CBF5E75C354)
            Map<String, // date (01.01.2021)
                Map<
                    String, // time (10:00)
                    Slot
                >
            >
        >
    > getSchedule(
        @RequestParam("service_id") String serviceId,
        @RequestParam @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate start,
        @RequestParam @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate end
    );

    @RequestMapping(
        value = "/bookings",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        method = RequestMethod.POST
    )
    ResponseEntity<BookingResponse> createBooking(@NotNull @Valid @RequestBody BookingRequest body);

    @Data
    class Slot {
        private String id;
        private boolean free;
        private Integer price;
    }

    @Data
    class BookingRequest {
        private Integer count = 2;
        private Integer integration = 1;
        private String comment;
        private String email;
        private String name;
        private String phone;
        private String service_id;
        private String slots_id;
        private String source_id;
    }

    @Data
    class BookingResponse {
        private String id;
        private Integer price;
        private Integer paid_price;
        private String name;
        private String email;
    }
}
