package com.example

import com.example.dto.AnalyseResponse
import com.example.dto.Candle
import com.example.services.AnalyseService
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        post("/analyse") {
            val candles = call.receive<Array<Candle>>()
            val service = AnalyseService()
            val profitability = service.profitability(candles)
            val result = AnalyseResponse(
                profitability = profitability,
                stdDev = service.stdDev(profitability),
                coefVariation = service.coefVariation(profitability),
                coefSharp = service.coefSharp(profitability, 0.02),
                coefInformation = service.coefInformation(profitability, 0.01),
                coefSortino = service.coefSortino(profitability, 0.02)
            )
            call.respond(result)
        }
    }
}
