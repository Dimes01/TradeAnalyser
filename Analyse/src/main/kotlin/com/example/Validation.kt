package com.example

import com.example.dto.AnalyseRequest
import com.example.dto.Candle
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import java.math.BigDecimal

fun Application.configureValidation() {
    install(RequestValidation) {
        validate<AnalyseRequest> {
            if (it.riskFree <= 0)
                ValidationResult.Invalid("Risk free should be non-negative")
            else if (it.meanBenchmark <= 0)
                ValidationResult.Invalid("Risk free should be non-negative")
            else {
                for (candle in it.candles) {
                    val result = validateCandle(candle)
                    if (result is ValidationResult.Invalid) {
                        return@validate result
                    }
                }
                ValidationResult.Valid
            }
        }

        validate<Candle> {
            validateCandle(it)
        }
    }
}

fun validateCandle(candle: Candle): ValidationResult {
    return when {
        candle.open <= BigDecimal.ZERO -> ValidationResult.Invalid("Open price should be positive")
        candle.close <= BigDecimal.ZERO -> ValidationResult.Invalid("Close price should be positive")
        candle.high <= BigDecimal.ZERO -> ValidationResult.Invalid("High price should be positive")
        candle.low <= BigDecimal.ZERO -> ValidationResult.Invalid("Low price should be positive")
        candle.volume <= 0 -> ValidationResult.Invalid("Volume should be positive")
        candle.candleSourceType !in 0..2 -> ValidationResult.Invalid("CandleSourceType should be in the range from 0 to 2")
        else -> ValidationResult.Valid
    }
}