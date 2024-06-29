package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.config.*
import org.jetbrains.exposed.sql.Database


fun Application.configureDatabases(config: ApplicationConfig) {
    val type = config.property("storage.type").getString()
    println("**** DB_TYPE - $type")
    val url = config.property("storage.url").getString()
    println("**** DB_URL - $url")
    val user = config.property("storage.user").getString()
    println("**** DB_USER - $user")
    val password = config.property("storage.password").getString()
    println("**** DB_PASSWORD - $password")

    Database.connect(
        url = url,
        user = user,
        password = password
    )
}