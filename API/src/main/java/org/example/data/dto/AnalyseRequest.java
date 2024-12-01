package org.example.data.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyseRequest {
    @Size(min = 1, message = "At least one candle is required")
    private List<HistoricCandleDTO> candles;

    @Positive(message = "Risk free rate must be positive")
    private Double riskFree;

    @Positive(message = "Mean benchmark must be positive")
    private Double meanBenchmark;
}
