package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class SettingsRequest {
    @NotBlank(message = "Account id can not be blank")
    private String accountId;

    @Positive(message = "Risk free must be positive")
    private double riskFree;

    @Positive(message = "Mean of benchmark must be positive")
    private double meanBenchmark;
}
