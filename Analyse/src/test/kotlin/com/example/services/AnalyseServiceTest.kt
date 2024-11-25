package com.example.services

import com.example.dto.Candle
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Instant
import java.util.stream.Stream


class AnalyseServiceTest {
    private companion object {
        val candles: Array<Candle> = arrayOf(
            candle(100.0,103.0,105.0,96.0,"2024-11-15T11:00:00Z"),
            candle(103.0,98.0,105.0,96.0,"2024-11-15T12:00:00Z"),
            candle(98.0,108.0,110.0,96.0,"2024-11-15T13:00:00Z")
        )
        val profits: Array<Double> = arrayOf(
            profit(candles[0].close, candles[0].open),
            profit(candles[1].close, candles[1].open),
            profit(candles[2].close, candles[2].open)
        )

        @JvmStatic
        fun profitsWithMaxProfit(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(arrayOf(profits[0], profits[1], profits[2]), BigDecimal(0.5)),
                Arguments.of(arrayOf(profits[0], profits[1], 0.0), BigDecimal(0.04)),
                Arguments.of(arrayOf(0.0, 0.0, 0.0), BigDecimal(-1))
            )
        }

        fun candle(
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

        fun profit(first: BigDecimal, end: BigDecimal): Double = first.divide(end, 2, RoundingMode.HALF_UP).toDouble() - 1
    }
    private val service: AnalyseService = AnalyseService()


    @Test
    fun profitability_arrayFilled_arrayFilled() {
        // Arrange
        val expected: Array<Double> = arrayOf(profits[0], profits[1], profits[2])

        // Act
        val profits = service.profitability(candles)

        // Assert
        assertTrue(expected contentEquals profits)
    }

    @Test
    fun profitability_emptyArray_emptyArray() {
        // Arrange & Act
        val profits = service.profitability(emptyArray())

        // Assert
        assertTrue(profits.isEmpty())
    }

    @ParameterizedTest
    @MethodSource("profitsWithMaxProfit")
    fun profitabilityWithMaxProfit_arrayFilled_arrayFilled(expected: Array<Double>, maxProfit: BigDecimal) {
        // Arrange & Act
        val profits = service.profitability(candles, maxProfit)

        // Assert
        assertTrue(expected contentEquals profits)
    }
}