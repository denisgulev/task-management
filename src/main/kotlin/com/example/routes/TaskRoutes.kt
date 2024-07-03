package com.example.routes

import com.example.domain.entity.Task
import com.example.domain.entity.toResponse
import com.example.domain.enum.Priority
import com.example.domain.repository.ITaskRepository
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.types.ObjectId
import org.koin.ktor.ext.inject

fun Route.taskRoutes() {
    val repository by inject<ITaskRepository>()

    route("/task") {
        get {
            repository.allTasks().also {
                call.respond(it)
            }
        }

        get("/{id?}") {
            val id = call.parameters["id"]
            if (id.isNullOrEmpty()) {
                return@get call.respondText(
                    text = "Missing id",
                    status = HttpStatusCode.BadRequest
                )
            }
            repository.findById(ObjectId(id))?.let {
                call.respond(it.toResponse())
            } ?: call.respondText("No records found for id $id")
        }

        get("/byName/{taskName?}") {
            val name = call.parameters["taskName"]
            if (name.isNullOrEmpty()) {
                return@get call.respondText(
                    text = "Missing name",
                    status = HttpStatusCode.BadRequest
                )
            }
            repository.findByName(name)?.let {
                call.respond(it.toResponse())
            } ?: call.respond(HttpStatusCode.NotFound)
        }

        get("/byPriority/{priority}") {
            val priorityAsText = call.parameters["priority"]
            if (priorityAsText.isNullOrEmpty()) {
                return@get call.respondText(
                    text = "Missing priority",
                    status = HttpStatusCode.BadRequest
                )
            }
            repository.findByPriority(Priority.valueOf(priorityAsText)).also {
                call.respond(it)
            }
        }

        post {
            try {
                val task = call.receive<Task>()
                val insertedId = repository.addTask(task)
                call.respond(HttpStatusCode.Created, "Created task with id $insertedId")
            } catch (ex: IllegalStateException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (ex: JsonConvertException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        delete("/{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respondText(
                text = "Missing task id",
                status = HttpStatusCode.BadRequest
            )
            val deleted: Boolean = repository.removeTask(ObjectId(id))
            if (deleted) {
                return@delete call.respondText("Task Deleted successfully", status = HttpStatusCode.OK)
            }
            return@delete call.respondText("Task not found", status = HttpStatusCode.NotFound)
        }

        patch("/{id?}") {
            val id = call.parameters["id"] ?: return@patch call.respondText(
                text = "Missing task id",
                status = HttpStatusCode.BadRequest
            )
            val updated = repository.updateTask(ObjectId(id), call.receive<Task>())
            call.respondText(
                text = if (updated == 1L) "Task updated successfully" else "Task not found",
                status = if (updated == 1L) HttpStatusCode.OK else HttpStatusCode.NotFound
            )
        }
    }
}