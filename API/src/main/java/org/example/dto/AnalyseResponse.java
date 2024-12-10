package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyseResponse {
    @JsonProperty("securities_id")
    private String figi;

    @JsonProperty("date_from")
    private Instant dateFrom;

    @JsonProperty("date_to")
    private Instant dateTo;

    @JsonProperty("probabilities")
    private List<Double> probabilities;

    @JsonProperty("mean")
    private Double mean;

    @JsonProperty("std_dev")
    private Double stdDev;

    @JsonProperty("coef_variation")
    private Double coefVariation;

    @JsonProperty("coef_sharp")
    private Double coefSharp;

    @JsonProperty("coef_information")
    private Double coefInformation;

    @JsonProperty("coef_sortino")
    private Double coefSortino;
}
