package com.example.dto

import kotlinx.serialization.Serializable

@Serializable
data class AnalyseResponse(
    val profitability: Array<Double>,
    val stdDev: Double,
    val coefVariation: Double,
    val coefSharp: Double,
    val coefInformation: Double,
    val coefSortino: Double
)
