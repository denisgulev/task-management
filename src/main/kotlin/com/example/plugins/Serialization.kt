//package com.example.plugins
//
//import com.example.domain.entity.Task
//import com.example.domain.enum.Priority
//import com.example.domain.repository.ITaskRepository
//import io.ktor.http.*
//import io.ktor.serialization.*
//import io.ktor.serialization.gson.*
//import io.ktor.server.application.*
//import io.ktor.server.plugins.contentnegotiation.*
//import io.ktor.server.request.*
//import io.ktor.server.response.*
//import io.ktor.server.routing.*
//import org.koin.ktor.ext.inject
//
//fun Application.configureSerialization() {
//    val repository by inject<ITaskRepository>()
//    install(ContentNegotiation) {
//        gson()
//    }
//    routing {
//        route("/tasksql") {
//            get {
//                val tasks = repository.allTasks()
//                call.respond(tasks)
//            }
//
//            get("/byName/{taskName}") {
//                val name = call.parameters["taskName"]
//                if (name == null) {
//                    call.respond(HttpStatusCode.BadRequest)
//                    return@get
//                }
//                val task = repository.findByName(name)
//                if (task == null) {
//                    call.respond(HttpStatusCode.NotFound)
//                    return@get
//                }
//                call.respond(task)
//            }
//
//            get("/byPriority/{priority}") {
//                val priorityAsText = call.parameters["priority"]
//                if (priorityAsText == null) {
//                    call.respond(HttpStatusCode.BadRequest)
//                    return@get
//                }
//                try {
//                    val priority = Priority.valueOf(priorityAsText)
//                    val tasks = repository.findByPriority(priority)
//
//                    if (tasks.isEmpty()) {
//                        call.respond(HttpStatusCode.NotFound)
//                        return@get
//                    }
//                    call.respond(tasks)
//                } catch (ex: IllegalArgumentException) {
//                    call.respond(HttpStatusCode.BadRequest)
//                }
//            }
//
//            post {
//                try {
//                    val task = call.receive<Task>()
//                    repository.addTask(task)
//                    call.respond(HttpStatusCode.NoContent)
//                } catch (ex: IllegalStateException) {
//                    call.respond(HttpStatusCode.BadRequest)
//                } catch (ex: JsonConvertException) {
//                    call.respond(HttpStatusCode.BadRequest)
//                }
//            }
//
//            delete("/{taskName}") {
//                val name = call.parameters["taskName"]
//                if (name == null) {
//                    call.respond(HttpStatusCode.BadRequest)
//                    return@delete
//                }
//                if (repository.removeTask(name)) {
//                    call.respond(HttpStatusCode.NoContent)
//                } else {
//                    call.respond(HttpStatusCode.NotFound)
//                }
//            }
//        }
//    }
//}
