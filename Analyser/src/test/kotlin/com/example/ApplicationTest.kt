package com.example

import com.example.dto.AnalyseRequest
import com.example.dto.AnalyseResponse
import com.example.dto.Candle
import com.example.services.AnalyseService
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.math.BigDecimal
import java.time.Instant
import kotlin.test.*

class ApplicationTest {
    private val service: AnalyseService = AnalyseService()
    private val candles: List<Candle> = listOf(
        candle(100.1,103.2,105.3,96.4,"2024-11-15T11:00:00Z"),
        candle(103.2,98.3,105.4,96.5,"2024-11-15T12:00:00Z"),
        candle(98.3,108.4,110.5,96.6,"2024-11-15T13:00:00Z")
    )
    private val profits: Array<Double> = service.profitability(candles)
    private val riskFree: Double = 0.01
    private val meanBenchmark: Double = 0.015
    private val request: AnalyseRequest = AnalyseRequest(candles, riskFree, meanBenchmark)
    private val expectedResponse: AnalyseResponse = AnalyseResponse(
        profitability = profits.toList(),
        mean = profits.average(),
        stdDev = service.stdDev(profits),
        coefVariation = service.coefVariation(profits),
        coefSharp = service.coefSharp(profits, riskFree),
        coefInformation = service.coefInformation(profits, meanBenchmark),
        coefSortino = service.coefSortino(profits, riskFree)
    )

    @Test
    fun testRoot() = testApplication {
        application {
            configureSerialization()
            configureRouting()
            configureValidation()
        }
        install(ContentNegotiation) { json() }
        client.post("/analyse"){
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(request))
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(expectedResponse, this.body<AnalyseResponse>())
        }
    }

    private fun candle(
        open: Double, close: Double,
        high: Double, low: Double,
        time: String,
        isComplete: Boolean = true,
        volume: Int = 100,
        candleSourceType: Int = 1): Candle =
        Candle(open = BigDecimal(open),
            close = BigDecimal(close),
            high = BigDecimal(high),
            low = BigDecimal(low),
            time = Instant.parse(time),
            isComplete = isComplete,
            volume = volume,
            candleSourceType = candleSourceType)
}
