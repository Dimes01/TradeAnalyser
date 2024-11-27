package com.example.services

import com.example.dto.Candle
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Instant
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class AnalyseServiceTest {
    private val eps: Double = 0.000001
    private val service: AnalyseService = AnalyseService()
    private val candles: List<Candle> = listOf(
        candle(100.1, 103.2, 105.3, 96.4, "2024-11-15T11:00:00Z"),
        candle(103.2, 98.3, 105.4, 96.5, "2024-11-15T12:00:00Z"),
        candle(98.3, 108.4, 110.5, 96.6, "2024-11-15T13:00:00Z")
    )

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

    @Test
    fun testAllParameters() {
        val profits: Array<Double> = service.profitability(candles)
        val mean = profits.average()
        val meanBenchmark = 0.025
        val riskFree = 0.01
        val diff = profits.map { it - meanBenchmark}
        val riskF = profits.map { it - riskFree }

        val expectedStdDev = sqrt(profits.sumOf { (it - profits.average()).pow(2) } / profits.size)
        val expectedStdDevDiff = sqrt(diff.sumOf { (it - diff.average()).pow(2) } / diff.size)
        val expectedCoefVariation = expectedStdDev / mean
        val expectedCoefSharp = riskF.average() / expectedStdDev
        val expectedCoefInformation = diff.average() / expectedStdDevDiff
        val expectedCoefSortino = riskF.average() / service.stdDev(profits.map { if (it <= riskFree) it else 0.0 }.toTypedArray())

        val receivedStdDev = service.stdDev(profits)
        val receivedStdDevDiff = service.stdDevDiff(profits, meanBenchmark)
        val receivedCoefVariation = service.coefVariation(profits)
        val receivedCoefSharp = service.coefSharp(profits, riskFree)
        val receivedCoefInformation = service.coefInformation(profits, meanBenchmark)
        val receivedCoefSortino = service.coefSortino(profits, riskFree)

        assertTrue(abs(expectedStdDev - receivedStdDev) < eps)
        assertTrue(abs(expectedStdDevDiff - receivedStdDevDiff) < eps)
        assertTrue(abs(expectedCoefVariation - receivedCoefVariation) < eps)
        assertTrue(abs(expectedCoefSharp - receivedCoefSharp) < eps)
        assertTrue(abs(expectedCoefInformation - receivedCoefInformation) < eps)
        assertTrue(abs(expectedCoefSortino - receivedCoefSortino) < eps)
    }

    @Test
    fun profitability_correctCandles_correctProfits() {
        // Arrange
        val expectedProfits: Array<Double> = arrayOf(
            candles[0].close.divide(candles[0].open, 2, RoundingMode.HALF_UP).toDouble() - 1,
            candles[1].close.divide(candles[1].open, 2, RoundingMode.HALF_UP).toDouble() - 1,
            candles[2].close.divide(candles[2].open, 2, RoundingMode.HALF_UP).toDouble() - 1,
        )

        // Act
        val receivedProfits = service.profitability(candles)

        // Assert
        for (i in expectedProfits.indices)
            assertTrue(abs(expectedProfits[i] - receivedProfits[i]) < eps)
        assertEquals(expectedProfits.size, receivedProfits.size)
    }

    @Test
    fun profitability_incorrectCandles_throwArithmeticException() {
        // Arrange
        val incorrectCandles: List<Candle> = listOf(candle(0.0, 103.2, 105.3, 96.4, "2024-11-15T11:00:00Z"))

        // Assert & Act
        assertThrows<ArithmeticException>({ service.profitability(incorrectCandles) })
    }

    @Test
    fun profitabilityWithMaxProfit_correctCandles_correctProfits() {
        // Arrange
        val maxProfit: Double = 0.1
        val expectedProfits: Array<Double> = arrayOf(
            candles[0].close.divide(candles[0].open, 2, RoundingMode.HALF_UP).toDouble() - 1,
            candles[1].close.divide(candles[1].open, 2, RoundingMode.HALF_UP).toDouble() - 1,
            candles[2].close.divide(candles[2].open, 2, RoundingMode.HALF_UP).toDouble() - 1,
        )

        // Act
        val receivedProfits = service.profitability(candles)

        // Assert
        for (i in expectedProfits.indices)
            assertTrue(abs(expectedProfits[i] - receivedProfits[i]) < eps)
        assertEquals(expectedProfits.size, receivedProfits.size)
    }
}