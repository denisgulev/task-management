package com.example

import com.example.plugins.*
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>) = EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.module() {
    val client = configureHttpClient()
    configureDI(environment)
    configureContentNegotiation()
    configureRouting(client)
}
