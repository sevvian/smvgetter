package com.extractor.api

import com.extractor.api.plugins.configureRouting
import com.extractor.api.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        log.info("Ktor server is starting up...")
        configureSerialization()
        configureRouting()
        log.info("Ktor server modules configured.")
    }.start(wait = true)
}