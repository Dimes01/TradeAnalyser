package com.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.tinkoff.piapi.contract.v1.HistoricCandle
import ru.tinkoff.piapi.contract.v1.MoneyValue

@SpringBootApplication
class AnalyseApplication

fun main(args: Array<String>) {
    runApplication<AnalyseApplication>(*args)
}