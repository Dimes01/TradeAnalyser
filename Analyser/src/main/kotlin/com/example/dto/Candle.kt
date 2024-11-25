package com.example.dto

import com.example.utilities.BigDecimalSerializer
import com.example.utilities.InstantSerializer
import java.math.BigDecimal
import java.time.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Candle(
    @Serializable(with = BigDecimalSerializer::class)
    val open: BigDecimal,

    @Serializable(with = BigDecimalSerializer::class)
    val close: BigDecimal,

    @Serializable(with = BigDecimalSerializer::class)
    val high: BigDecimal,

    @Serializable(with = BigDecimalSerializer::class)
    val low: BigDecimal,

    @Serializable(with = InstantSerializer::class)
    val time: Instant,

    val isComplete: Boolean,

    val volume: Int,

    val candleSourceType: Int,
)