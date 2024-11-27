package com.example.dto

import kotlinx.serialization.Serializable

@Serializable
data class AnalyseRequest(
    val candles: List<Candle>,
    val riskFree: Double,
    val meanBenchmark: Double,
)
