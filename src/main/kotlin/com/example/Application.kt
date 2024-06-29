package com.example

import com.example.model.PostgresTaskRepository
import com.example.plugins.configureDatabases
import com.example.plugins.configureRouting
import com.example.plugins.configureSerialization
import com.example.plugins.configureTemplating
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val repository = PostgresTaskRepository()

    configureDatabases(environment.config)
    configureSerialization(repository)
    configureTemplating(repository)
    configureRouting()
}
