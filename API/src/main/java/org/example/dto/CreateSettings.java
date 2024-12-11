package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSettings {

    @NotBlank(message = "Account id can not be blank")
    @JsonProperty("account_id")
    private String accountId;

    @Positive(message = "Risk free must be positive")
    @JsonProperty("risk_free")
    private double riskFree;

    @Positive(message = "Mean of benchmark must be positive")
    @JsonProperty("mean_benchmark")
    private double meanBenchmark;
}
