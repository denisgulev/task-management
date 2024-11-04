package com.example.plugins

import com.mongodb.kotlin.client.coroutine.MongoClient
import io.ktor.server.application.*
import io.ktor.server.config.*
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class DatabaseModule() {
    @Single
    fun provideMongoClient(config: ApplicationConfig, environment: ApplicationEnvironment): MongoClient {
        return  createMongoClient(config, environment)
    }
}

fun createMongoClient(config: ApplicationConfig, environment: ApplicationEnvironment): MongoClient {
    val type = config.property("storage.type").getString()
    println("**** DB_TYPE - $type")
    val url: String = config.property("storage.url").getString()
    println("**** DB_URL - $url")
    val user = config.property("storage.user").getString()
    println("**** DB_USER - $user")
    val password = config.property("storage.password").getString()
    println("**** DB_PASSWORD - $password")
    val name = config.property("storage.name").getString()
    println("**** DB_NAME - $name")

    val mongoClient = MongoClient.create(url)
    environment.monitor.subscribe(ApplicationStopped) {
        mongoClient.close()
    }
    return mongoClient
}