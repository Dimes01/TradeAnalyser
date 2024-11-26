package com.example

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * **Исключение:** Please make sure that you use unique name for the plugin and don't install it twice. Conflicting application
 * plugin is already installed with the same key as ContentNegotiation
 */
@Deprecated("Возникнет исключение, связанное с повторной установкой плагина")
fun Application.configureSerialization() {
//    install(ContentNegotiation) {
//        json()
//    }
}
