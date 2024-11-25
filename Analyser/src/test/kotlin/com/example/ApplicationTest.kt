package com.example

import com.example.dto.Candle
import com.example.services.AnalyseService
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import java.math.BigDecimal
import java.time.Instant
import kotlin.test.*

class ApplicationTest {
    private val service: AnalyseService = AnalyseService()
    private val candles: Array<Candle> = arrayOf(
        candle(100.0,103.0,105.0,96.0,"2024-11-15T11:00:00Z"),
        candle(103.0,98.0,105.0,96.0,"2024-11-15T12:00:00Z"),
        candle(98.0,108.0,110.0,96.0,"2024-11-15T13:00:00Z")
    )
    private val profits: Array<Double> = service.profitability(candles)

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
            setBody(candles)
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
//            assertEquals(profits, this)
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
