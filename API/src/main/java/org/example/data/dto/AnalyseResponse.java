package org.example.data.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyseResponse {
    private List<Double> probabilities;
    private Double mean;
    private Double stdDev;
    private Double coefVariation;
    private Double coefSharp;
    private Double coefInformation;
    private Double coefSortino;
}
