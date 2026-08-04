package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TableRecord(
        @JsonProperty("id") String id,
        @JsonProperty("date") String date,
        @JsonProperty("status") String status,
        @JsonProperty("email") String email,
        @JsonProperty("amount") String amount
) {
}