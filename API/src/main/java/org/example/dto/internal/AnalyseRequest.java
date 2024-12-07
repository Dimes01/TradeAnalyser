package org.example.dto.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.HistoricCandleDTO;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyseRequest {
    @JsonProperty("candles")
    private List<HistoricCandleDTO> candles;

    @JsonProperty("risk_free")
    private Double riskFree;

    @JsonProperty("mean_benchmark")
    private Double meanBenchmark;
}
