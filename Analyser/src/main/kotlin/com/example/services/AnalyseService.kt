package com.example.services

import com.example.dto.Candle
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.pow
import kotlin.math.sqrt

class AnalyseService {
    private companion object {
        val logger: Logger = LoggerFactory.getLogger(AnalyseService::class.java)
    }

    fun profitability(candles: List<Candle>): Array<Double> {
        logger.info("Method 'profitability': start")
        val profits = Array(candles.size) { 0.0 }
        for (i in candles.indices) {
            profits[i] = candles[i].close.divide(candles[i].open, 2, RoundingMode.HALF_UP).toDouble() - 1
        }
        return profits.also { logger.info("Method 'profitability': finish") }
    }

    fun profitability(candles: Array<Candle>, maxProfit: BigDecimal): Array<Double> {
        logger.info("Method 'profitability' with max profit: start")
        val mp = maxProfit.plus(BigDecimal(1.0))
        val profits = Array(candles.size) { 0.0 }
        for (i in candles.indices) {
            val profit = candles[i].close.divide(candles[i].open, 2, RoundingMode.HALF_UP)
            if (profit.compareTo(mp) == -1)
                profits[i] = profit.toDouble() - 1
        }
        return profits.also { logger.info("Method 'profitability' with max profit: finish") }
    }

    fun stdDev(profitability: Array<Double>): Double {
        logger.info("Method 'stdDev': start")
        val mean = profitability.average()
        return sqrt(profitability.map{ (it - mean).pow(2) }.average()).also { logger.info("Method 'stdDev': finish") }
    }

    fun stdDevDiff(profitability: Array<Double>, meanBenchmark: Double): Double {
        logger.info("Method 'stdDevDiff': start")
        val diff = profitability.map { it - meanBenchmark }
        val mean = diff.average()
        return sqrt(diff.map{ (it - mean).pow(2) }.average()).also { logger.info("Method 'stdDevDiff': finish") }
    }

    fun coefVariation(profitability: Array<Double>): Double {
        logger.info("Method 'coefVariation': start")
        val stdDev = stdDev(profitability)
        val mean = profitability.average()
        return (stdDev / mean).also { logger.info("Method 'coefVariation': finish") }
    }

    fun coefSharp(profitability: Array<Double>, riskFree: Double): Double {
        logger.info("Method 'coefSharp': start")
        val mean = profitability.map { it - riskFree }.average()
        val stdDev = stdDev(profitability)
        return (mean / stdDev).also { logger.info("Method 'coefSharp': finish") }
    }

    fun coefInformation(profitability: Array<Double>, meanBenchmark: Double): Double {
        logger.info("Method 'coefInformation': start")
        val mean = profitability.map { it - meanBenchmark }.average()
        val stdDev = stdDevDiff(profitability, meanBenchmark)
        return (mean / stdDev).also { logger.info("Method 'coefInformation': finish") }
    }

    fun coefSortino(profitability: Array<Double>, riskFree: Double): Double {
        logger.info("Method 'coefSortino': start")
        val mean = profitability.map { it - riskFree }.average()
        val stdDev = stdDev(profitability.map { if (it <= riskFree) it else 0.0 }.toTypedArray())
        return (mean / stdDev).also { logger.info("Method 'coefSortino': finish") }
    }
}