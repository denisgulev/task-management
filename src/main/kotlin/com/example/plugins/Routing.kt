package com.example.plugins

import com.example.routes.taskRoutes
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(client: HttpClient) {
    routing {
        taskRoutes(client)
    }
}

fun configureHttpClient(): HttpClient {
    return HttpClient(CIO) {
        // Configure the client as needed
    }
}