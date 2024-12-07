package com.example.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnalyseRequest(
    @SerialName("candles")
    val candles: List<Candle>,

    @SerialName("risk_free")
    val riskFree: Double,

    @SerialName("mean_benchmark")
    val meanBenchmark: Double,
)
