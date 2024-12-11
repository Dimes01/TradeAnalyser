package com.example.dto

import com.example.utilities.InstantSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class AnalyseResponse(
    @SerialName("profitability")
    val profitability: List<Double>,

    @SerialName("mean")
    val mean: Double,

    @SerialName("std_dev")
    val stdDev: Double,

    @SerialName("coef_variation")
    val coefVariation: Double,

    @SerialName("coef_sharp")
    val coefSharp: Double,

    @SerialName("coef_information")
    val coefInformation: Double,

    @SerialName("coef_sortino")
    val coefSortino: Double,
)
