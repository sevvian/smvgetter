package com.extractor.api.plugins

import com.extractor.api.ExtractorLogic
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.statuspages.*

fun Application.configureRouting() {
    // Added verbose logging to trace execution flow
    log.info("Configuring application routing...")

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            // Added logging to the global exception handler
            log.error("An unhandled exception occurred", cause)
            call.respondText(text = "500: ${cause.message}" , status = HttpStatusCode.InternalServerError)
        }
    }
    routing {
        // API endpoint for extraction
        get("/extract") {
            // Added logging to the beginning of the route handler
            log.info("Received /extract request with URL: ${call.request.queryParameters["url"]}")

            val url = call.request.queryParameters["url"]
            if (url.isNullOrBlank()) {
                log.warn("/extract request failed: URL parameter is missing.")
                call.respond(HttpStatusCode.BadRequest, "URL parameter is required.")
                return@get
            }

            try {
                log.info("Calling ExtractorLogic for URL: $url")
                val links = ExtractorLogic.extract(url)
                if (links.isNotEmpty()) {
                    log.info("Successfully extracted ${links.size} links for URL: $url")
                    call.respond(links)
                } else {
                    log.warn("No stream links found for URL: $url")
                    call.respond(HttpStatusCode.NotFound, "No stream links found.")
                }
            } catch (e: Exception) {
                // This will be caught by StatusPages, but logging here provides more context
                log.error("Extraction failed in /extract handler for URL: $url", e)
                // The response is handled by the StatusPages plugin
                throw e
            }
        }

        // Serve the static frontend
        staticResources("/", "static")
    }
    
    log.info("Application routing configured successfully.")
}