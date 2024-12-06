package com.example

import com.example.dto.AnalyseRequest
import com.example.dto.AnalyseResponse
import com.example.dto.Candle
import com.example.services.AnalyseService
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.math.BigDecimal
import java.time.Instant
import java.util.stream.Stream
import kotlin.test.assertEquals


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

    companion object {
        @JvmStatic
        fun invalidCandleProvider(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(candle(0.0, 103.2, 105.3, 96.4, "2024-11-15T11:00:00Z")),
                Arguments.of(candle(-1.0, 103.2, 105.3, 96.4, "2024-11-15T11:00:00Z")),
                Arguments.of(candle(100.1, 0.0, 105.3, 96.4, "2024-11-15T11:00:00Z")),
                Arguments.of(candle(100.1, -1.0, 105.3, 96.4, "2024-11-15T11:00:00Z")),
                Arguments.of(candle(100.1, 103.2, 0.0, 96.4, "2024-11-15T11:00:00Z")),
                Arguments.of(candle(100.1, 103.2, -1.0, 96.4, "2024-11-15T11:00:00Z")),
                Arguments.of(candle(100.1, 103.2, 105.3, 0.0, "2024-11-15T11:00:00Z")),
                Arguments.of(candle(100.1, 103.2, 105.3, -1.0, "2024-11-15T11:00:00Z")),
                Arguments.of(candle(100.1, 103.2, 105.3, 96.4, "2024-11-15T11:00:00Z", volume = 0)),
                Arguments.of(candle(100.1, 103.2, 105.3, 96.4, "2024-11-15T11:00:00Z", volume = -1)),
                Arguments.of(candle(100.1, 103.2, 105.3, 96.4, "2024-11-15T11:00:00Z", candleSourceType = -1)),
                Arguments.of(candle(100.1, 103.2, 105.3, 96.4, "2024-11-15T11:00:00Z", candleSourceType = 3))
            )
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

    private fun analyseResponse(profits: Array<Double>, riskFree: Double, meanBenchmark: Double): AnalyseResponse =
        AnalyseResponse(
            profitability = profits.toList(),
            mean = profits.average(),
            stdDev = service.stdDev(profits),
            coefVariation = service.coefVariation(profits),
            coefSharp = service.coefSharp(profits, riskFree),
            coefInformation = service.coefInformation(profits, meanBenchmark),
            coefSortino = service.coefSortino(profits, riskFree)
        )

    private fun testInvalidCandle(candle: Candle) = testApplication {
        application { this.module() }
        assertThrows<RequestValidationException> {
            val incorrectRequest = AnalyseRequest(
                candles = listOf(candle),
                riskFree = riskFree,
                meanBenchmark = meanBenchmark
            )
            client.post("/analyse") {
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(incorrectRequest))
            }
        }
    }

    @Test
    fun analyse_correctRequest_correctResponse() = testApplication {
        application{ this.module() }
        client.post("/analyse"){
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(AnalyseRequest(candles, riskFree, meanBenchmark)))
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(analyseResponse(profits, riskFree, meanBenchmark), Json.decodeFromString<AnalyseResponse>(this.bodyAsText()) )
        }
    }

    @Test
    fun analyse_incorrectRiskFree_throwRequestValidationException() = testApplication {
        application { this.module() }
        assertThrows<RequestValidationException> {
            client.post("/analyse"){
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(AnalyseRequest(candles, -1.0, meanBenchmark)))
            }
        }
    }

    @Test
    fun analyse_incorrectMeanBenchmark_throwRequestValidationException() = testApplication {
        application { this.module() }
        assertThrows<RequestValidationException> {
            client.post("/analyse"){
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(AnalyseRequest(candles, riskFree, -1.0)))
            }
        }
    }

    @ParameterizedTest
    @MethodSource("invalidCandleProvider")
    fun analyse_invalidCandle_throwRequestValidationException(candle: Candle) {
        testInvalidCandle(candle)
    }
}
