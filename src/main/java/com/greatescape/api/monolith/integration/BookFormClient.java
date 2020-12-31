package com.greatescape.api.monolith.integration;

import com.greatescape.api.monolith.config.feign.GeneralClientConfiguration;
import java.time.LocalDate;
import java.util.Map;
import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "bookFormClient",
    url = "${app.book-form.base-url}/api/v1",
    configuration = GeneralClientConfiguration.class
)
public interface BookFormClient {

    @GetMapping(value = "/schedule", produces = "application/json")
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

    @Data
    class Slot {
        private String id;
        private boolean free;
        private Integer price;
    }
}
