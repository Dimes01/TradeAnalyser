package org.example.dto.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyseResponse {
    @JsonProperty("profitability")
    private List<Double> profitability;

    @JsonProperty("mean")
    private double mean;

    @JsonProperty("std_dev")
    private double stdDev;

    @JsonProperty("coef_variation")
    private double coefVariation;

    @JsonProperty("coef_sharp")
    private double coefSharp;

    @JsonProperty("coef_information")
    private double coefInformation;

    @JsonProperty("coef_sortino")
    private double coefSortino;
}
