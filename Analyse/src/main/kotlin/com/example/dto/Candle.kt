package com.example.dto

import com.example.utilities.BigDecimalSerializer
import com.example.utilities.InstantSerializer
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.PositiveOrZero
import java.math.BigDecimal
import java.time.Instant
import kotlinx.serialization.Serializable
import org.jetbrains.annotations.NotNull

@Serializable
data class Candle(
    @PositiveOrZero
    @Serializable(with = BigDecimalSerializer::class)
    val open: BigDecimal,

    @PositiveOrZero
    @Serializable(with = BigDecimalSerializer::class)
    val close: BigDecimal,

    @PositiveOrZero
    @Serializable(with = BigDecimalSerializer::class)
    val high: BigDecimal,

    @PositiveOrZero
    @Serializable(with = BigDecimalSerializer::class)
    val low: BigDecimal,

    @NotBlank
    @Serializable(with = InstantSerializer::class)
    val time: Instant,

    @NotNull
    val isComplete: Boolean,

    @PositiveOrZero
    val volume: Int,

    @Min(0) @Max(2)
    val candleSourceType: Int,
)