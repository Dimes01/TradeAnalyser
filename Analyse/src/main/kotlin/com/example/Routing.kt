package com.example

import com.example.dto.AnalyseRequest
import com.example.dto.AnalyseResponse
import com.example.services.AnalyseService
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        post("/analyse") {
            val request = call.receive<AnalyseRequest>()
            val service = AnalyseService()
            val profits = service.profitability(request.candles)
            val result = AnalyseResponse(
                profitability = profits.toList(),
                mean = profits.average(),
                stdDev = service.stdDev(profits),
                coefVariation = service.coefVariation(profits),
                coefSharp = service.coefSharp(profits, request.riskFree),
                coefInformation = service.coefInformation(profits, request.meanBenchmark),
                coefSortino = service.coefSortino(profits, request.riskFree)
            )
            log.info(result.toString())
            call.respond(result)
        }
    }
}
