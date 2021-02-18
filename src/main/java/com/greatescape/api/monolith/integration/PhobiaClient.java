package com.greatescape.api.monolith.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greatescape.api.monolith.config.feign.GeneralClientConfiguration;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "phobiaClient",
    url = "${app.integration.phobia.base-url}/api/v1",
    configuration = GeneralClientConfiguration.class
)
public interface PhobiaClient {

    @GetMapping(value = "/timetable", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<TimetableItem>> getSchedule(
        @RequestParam("start_date") @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate start,
        @RequestParam("end_date") @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate end,
        @RequestParam("quests") String questId
    );

    @Data
    @AllArgsConstructor
    class TimetableItem {
        private List<Slot> slotList;
        private QuestDetails questDetails;
    }

    class TimetableItemDeserializer extends JsonDeserializer<TimetableItem> {
        @Override
        public TimetableItem deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            JsonNode node = jp.readValueAsTree();
            ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);;

            return new TimetableItem(
                mapper.readValue(node.get(0).toString(), new TypeReference<>() {}),
                mapper.readValue(node.get(1).toString(), QuestDetails.class)
            );
        }
    }

    @Data
    class Slot {
        private String id;
        @JsonProperty("av") private boolean isAvailable;
        @JsonProperty("tm24") private String time;
        @JsonProperty("start_date") private String date;
        private Integer price;
    }

    @Data
    class QuestDetails {
        private Integer id;
        private String address;
    }
}
