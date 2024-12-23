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
            val mean = profits.average()
            val stdDev = service.stdDev(profits)
            val result: AnalyseResponse =
                if (mean == 0.0 || stdDev == 0.0)
                    AnalyseResponse(profits.toList(), 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
                else
                    AnalyseResponse(
                        profitability = profits.toList(),
                        mean = mean,
                        stdDev = stdDev,
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
