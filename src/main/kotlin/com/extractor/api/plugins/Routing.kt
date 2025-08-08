package com.extractor.api.plugins

import com.extractor.api.ExtractorLogic
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.statuspages.*

fun Application.configureRouting() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: ${cause.message}" , status = HttpStatusCode.InternalServerError)
        }
    }
    routing {
        // API endpoint for extraction
        get("/extract") {
            val url = call.request.queryParameters["url"]
            if (url.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, "URL parameter is required.")
                return@get
            }

            try {
                val links = ExtractorLogic.extract(url)
                if (links.isNotEmpty()) {
                    call.respond(links)
                } else {
                    call.respond(HttpStatusCode.NotFound, "No stream links found.")
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Extraction failed: ${e.message}")
            }
        }

        // Serve the static frontend
        staticResources("/", "static")
    }
}