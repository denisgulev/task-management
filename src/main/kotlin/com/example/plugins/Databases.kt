package com.example.plugins

import com.example.domain.repository.ITaskRepository
import com.example.repository.TaskRepositoryImpl
import com.mongodb.kotlin.client.coroutine.MongoClient
import io.ktor.server.application.*
import io.ktor.server.config.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger


fun Application.configureDatabases(config: ApplicationConfig) {
    val type = config.property("storage.type").getString()
    println("**** DB_TYPE - $type")
    val url: String? = config.property("storage.url").getString()
    println("**** DB_URL - $url")
    val user = config.property("storage.user").getString()
    println("**** DB_USER - $user")
    val password = config.property("storage.password").getString()
    println("**** DB_PASSWORD - $password")
    val name = config.property("storage.name").getString()
    println("**** DB_NAME - $name")

    install(Koin) {
        slf4jLogger()
        val dbModule = module {
            single { MongoClient.create(
                url ?: throw RuntimeException("Failed to access MongoDB URI.")
            ) }
            single { get<MongoClient>().getDatabase(name) }
        }
        val taskRepoModule = module {
            single<ITaskRepository> { TaskRepositoryImpl(get()) }
        }
        modules(dbModule, taskRepoModule)
    }
}