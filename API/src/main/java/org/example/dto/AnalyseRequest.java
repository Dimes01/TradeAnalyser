package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyseRequest {
    @NotBlank(message = "Identifier of securities is required")
    private String securities_id;

    // TODO: удалить и использовать только securities_id
    @Size(min = 1, message = "At least one candle of target securities is required")
    private List<HistoricCandleDTO> candles;

    @Positive(message = "Risk free rate must be positive")
    private Double riskFree;

    // TODO: изменить с фиксированного коэффициента на передачу ISIN бумаги-бенчмарка
    @Positive(message = "Mean benchmark rate must be positive")
    private Double meanBenchmark;
}
