package com.example.routes

import com.example.entities.*
import com.example.service.ITaskService
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.types.ObjectId
import org.koin.ktor.ext.inject

fun Route.taskRoutes() {
    val taskService by inject<ITaskService>()

    route("/task") {
        get {
            taskService.getAllTasks().also {
                call.respond(it)
            }
        }

        get("/{id?}") {
            val id = call.parameters["id"]
                ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing id")

            taskService.getTaskById(ObjectId(id))?.let {
                call.respond(it.toResponse())
            } ?: call.respond(HttpStatusCode.NotFound, "No records found for id $id")
        }

        get("/byName/{taskName?}") {
            val name = call.parameters["taskName"]
                ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing name")

            taskService.getTaskByName(name)?.let {
                call.respond(it.toResponse())
            } ?: call.respond(HttpStatusCode.NotFound, "No records found for name $name")
        }

        get("/byPriority/{priority}") {
            val priorityAsText = call.parameters["priority"]
                ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing priority")

            taskService.getTasksByPriority(Priority.valueOf(priorityAsText))
                .toList()
                .map { it.toResponse() }
                .let { call.respond(HttpStatusCode.OK, it) }
        }

        post {
            try {
                val task = call.receive<TaskRequest>()
                val insertedId = taskService.createTask(task.toDomain())
                call.respond(HttpStatusCode.Created, "Created task with id $insertedId")
            } catch (ex: IllegalStateException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (ex: JsonConvertException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        delete("/{id?}") {
            val id = call.parameters["id"]
                ?: return@delete call.respond(HttpStatusCode.BadRequest, "Missing id")
            val deleted: Boolean = taskService.deleteTask(ObjectId(id))
            if (deleted) {
                return@delete call.respond(HttpStatusCode.OK, "Task Deleted successfully")
            }
            return@delete call.respond(HttpStatusCode.NotFound, "Task not found")
        }

        patch("/{id?}") {
            val id = call.parameters["id"]
                ?: return@patch call.respond(HttpStatusCode.BadRequest, "Missing task id")
            val updated = taskService.updateTask(ObjectId(id), call.receive<Task>())
            return@patch if (updated == 1L)
                call.respond(HttpStatusCode.OK, "Task updated successfully")
            else
                call.respond(HttpStatusCode.NotFound, "Task not found")
        }
    }
}