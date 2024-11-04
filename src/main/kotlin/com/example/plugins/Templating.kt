package com.example.plugins

import com.example.entities.Task
import com.example.entities.toResponse
import com.example.entities.Priority
import com.example.repository.ITaskRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.get
import io.ktor.server.thymeleaf.*
import org.bson.types.ObjectId
import org.koin.ktor.ext.inject
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver

fun Application.configureTemplating() {
    val repository by inject<ITaskRepository>()
    install(Thymeleaf) {
        setTemplateResolver(ClassLoaderTemplateResolver().apply {
            prefix = "templates/thymeleaf/"
            suffix = ".html"
            characterEncoding = "utf-8"
        })
    }
    routing {
        staticResources("/", "static") {
            default("index.html")
        }
        get("/html-thymeleaf") {
            call.respond(ThymeleafContent("index", mapOf("user" to ThymeleafUser(1, "user1"))))
        }
        route("/tasks") {
            get {
                val tasks = repository.allTasks().map { it.toResponse() }
                println("********* TASKS: ${tasks.size} - ${tasks.forEach { 
                    println(it.id)
                    println(it.name)
                    println(it.description)
                    println(it.priority.toString())
                    print("*_*_*_*_*_*_*_*_*_*_*_*_*")
                }}")
                call.respond(ThymeleafContent("all-tasks", mapOf("tasks" to tasks)))
            }
            get("/byName") {
                val name = call.request.queryParameters["taskName"]
                if (name.isNullOrEmpty()) {
                    return@get call.respondText(
                        text = "Missing name",
                        status = HttpStatusCode.BadRequest
                    )
                }

                repository.findByName(name)?.let {
                    call.respond(ThymeleafContent("single-task", mapOf("task" to it.toResponse())))
                } ?: call.respondText(
                    text = "No records found for name $name",
                    status = HttpStatusCode.NotFound
                )
            }
            get("/byPriority") {
                val priorityAsText = call.request.queryParameters["priority"]
                if (priorityAsText.isNullOrEmpty()) {
                    return@get call.respondText(
                        text = "Missing priority",
                        status = HttpStatusCode.BadRequest
                    )
                }

                val priority = Priority.valueOf(priorityAsText)
                repository.findByPriority(priority).also {
                    if (it.isEmpty()) {
                        call.respondText(
                            text = "No records found for name $priorityAsText",
                            status = HttpStatusCode.NotFound
                        )
                    }
                    val data = mapOf(
                        "priority" to priority,
                        "tasks" to it
                    )
                    call.respond(ThymeleafContent("tasks-by-priority", data))
                }
            }

            post {
                val formContent = call.receiveParameters()
                val params = Triple(
                    formContent["name"] ?: "",
                    formContent["description"] ?: "",
                    formContent["priority"] ?: ""
                )
                if (params.toList().any { it.isEmpty() }) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                try {
                    val priority = Priority.valueOf(params.third)
                    repository.addTask(
                        Task(
                            id = ObjectId(),
                            name = params.first,
                            description = params.second,
                            priority = priority,
                        )
                    )
                    val tasks = repository.allTasks()
                    call.respond(ThymeleafContent("all-tasks", mapOf("tasks" to tasks)))
                } catch (ex: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (ex: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }
    }
}

data class ThymeleafUser(val id: Int, val name: String)
