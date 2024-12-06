package org.example.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyseRequest {
    // TODO: изменить на передачу ISIN бумаги
    @Size(min = 1, message = "At least one candle of target securities is required")
    private List<HistoricCandleDTO> candles;

    @Positive(message = "Risk free rate must be positive")
    private Double riskFree;

    // TODO: изменить с фиксированного коэффициента на передачу ISIN бумаги-бенчмарка
    @Positive(message = "Mean benchmark rate must be positive")
    private Double meanBenchmark;
}
