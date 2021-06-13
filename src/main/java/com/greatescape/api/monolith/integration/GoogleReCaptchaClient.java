package com.greatescape.api.monolith.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.greatescape.api.monolith.config.feign.GeneralClientConfiguration;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "googleReCaptchaClient",
    url = "${google.recaptcha.base-url}/recaptcha/api",
    configuration = GeneralClientConfiguration.class
)
public interface GoogleReCaptchaClient {

    @GetMapping(value = "/siteverify", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<VerifyResponse> verify(
        @RequestParam("secret") String secret,
        @RequestParam("response") String token
    );

    @Data
    @AllArgsConstructor
    class VerifyResponse {
        @JsonProperty("success") private boolean success;
        @JsonProperty("challenge_ts") private String challengeTs;
        @JsonProperty("hostname") private String hostname;
        @JsonProperty("score") private float score;
        @JsonProperty("action") private String action;
        @JsonProperty("error-codes") private List<ErrorCode> errorCodes;
    }

    enum ErrorCode {
        MISSING_SECRET("missing-input-secret"),
        INVALID_SECRET("invalid-input-secret"),
        MISSING_RESPONSE("missing-input-response"),
        INVALID_RESPONSE("invalid-input-response"),
        BAD_REQUEST("bad-request"),
        TIMEOUT_OR_DUPLICATE("timeout-or-duplicate"),
        ;

        private final String value;

        ErrorCode(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return this.value;
        }
    }
}
