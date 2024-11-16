package com.example.services

import com.example.dto.Candle
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class AnalyseService {
    private companion object {
        val logger: Logger = LoggerFactory.getLogger(AnalyseService::class.java)
    }

    fun profitability(candles: Array<Candle>): Array<Double> {
        logger.info("Method 'profitability': start")
        val profits = Array(candles.size) { 0.0 }
        for (i in candles.indices) {
            profits[i] = candles[i].close.divide(candles[i].open, 2, RoundingMode.HALF_UP).toDouble() - 1
        }
        logger.info("Method 'profitability': finish")
        return profits
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
        logger.info("Method 'profitability' with max profit: finish")
        return profits
    }
}