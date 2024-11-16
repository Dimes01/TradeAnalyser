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
            Candle(open = BigDecimal(100.0),
                close = BigDecimal(103.0),
                high = BigDecimal(105.0),
                low = BigDecimal(96.0),
                time = Instant.parse("2024-11-15T11:00:00Z"),
                isComplete = true,
                volume = 100,
                candleSourceType = 1),
            Candle(open = BigDecimal(103.0),
                close = BigDecimal(98.0),
                high = BigDecimal(105.0),
                low = BigDecimal(96.0),
                time = Instant.parse("2024-11-15T12:00:00Z"),
                isComplete = true,
                volume = 100,
                candleSourceType = 1),
            Candle(open = BigDecimal(98.0),
                close = BigDecimal(108.0),
                high = BigDecimal(110.0),
                low = BigDecimal(96.0),
                time = Instant.parse("2024-11-15T13:00:00Z"),
                isComplete = true,
                volume = 100,
                candleSourceType = 1)
        )

        @JvmStatic
        fun profitsWithMaxProfit(): Stream<Arguments> {
            val profit1 = profit(candles[0].close, candles[0].open)
            val profit2 = profit(candles[1].close, candles[1].open)
            val profit3 = profit(candles[2].close, candles[2].open)
            return Stream.of(
                Arguments.of(arrayOf(profit1, profit2, profit3), BigDecimal(0.5)),
                Arguments.of(arrayOf(profit1, profit2, 0.0), BigDecimal(0.04)),
                Arguments.of(arrayOf(0.0, 0.0, 0.0), BigDecimal(-1))
            )
        }

        fun profit(first: BigDecimal, end: BigDecimal): Double = first.divide(end, 2, RoundingMode.HALF_UP).toDouble() - 1
    }
    private val service: AnalyseService = AnalyseService()


    @Test
    fun profitability_arrayFilled_arrayFilled() {
        // Arrange
        val expected: Array<Double> = arrayOf(
            profit(candles[0].close, candles[0].open),
            profit(candles[1].close, candles[1].open),
            profit(candles[2].close, candles[2].open)
        )

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