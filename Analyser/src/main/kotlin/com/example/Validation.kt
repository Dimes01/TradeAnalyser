package com.example

import com.example.dto.Candle
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import java.math.BigDecimal

fun Application.configureValidation() {
    install(RequestValidation) {
        validate<Candle> {
            if (it.open <= BigDecimal.ZERO)
                ValidationResult.Invalid("Open price should be positive")
            else if (it.close <= BigDecimal.ZERO)
                ValidationResult.Invalid("Close price should be positive")
            else if (it.high <= BigDecimal.ZERO)
                ValidationResult.Invalid("High price should be positive")
            else if (it.low <= BigDecimal.ZERO)
                ValidationResult.Invalid("Low price should be positive")
            else if (it.volume <= 0)
                ValidationResult.Invalid("Volume should be positive")
            else if (it.candleSourceType !in 0..2)
                ValidationResult.Invalid("CandleSourceType should be in the range from 0 to 2")
            else
                ValidationResult.Valid
        }
    }
}