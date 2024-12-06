package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.utilities.serializers.CustomInstantDeserializer;
import org.example.utilities.serializers.CustomInstantSerializer;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoricCandleDTO {
    @DecimalMin(value = "0.0", inclusive = false, message = "Open price must be positive")
    @JsonDeserialize(using = NumberDeserializers.BigDecimalDeserializer.class)
    private BigDecimal open;

    @DecimalMin(value = "0.0", inclusive = false, message = "Close price must be positive")
    @JsonDeserialize(using = NumberDeserializers.BigDecimalDeserializer.class)
    private BigDecimal close;

    @DecimalMin(value = "0.0", inclusive = false, message = "High price must be positive")
    @JsonDeserialize(using = NumberDeserializers.BigDecimalDeserializer.class)
    private BigDecimal high;

    @DecimalMin(value = "0.0", inclusive = false, message = "Low price must be positive")
    @JsonDeserialize(using = NumberDeserializers.BigDecimalDeserializer.class)
    private BigDecimal low;

    @NotNull(message = "Time must be set")
    @JsonSerialize(using = CustomInstantSerializer.class)
    @JsonDeserialize(using = CustomInstantDeserializer.class)
    private Instant time;

    @JsonProperty("isComplete")
    @NotNull(message = "Is complete must be set")
    private boolean isComplete;

    @Positive(message = "Volume must be positive")
    private long volume;

    @Min(value = 0, message = "At least 0 required") @Max(value = 2, message = "At most 2 required")
    private int candleSourceType;
}
