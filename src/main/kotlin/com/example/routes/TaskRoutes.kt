package com.example.routes

import com.example.entities.*
import com.example.service.ITaskService
import com.example.service.UserService
import io.ktor.client.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import org.bson.types.ObjectId
import org.koin.ktor.ext.inject

fun Route.taskRoutes(client: HttpClient) {
    val taskService by inject<ITaskService>()
    val userService = UserService(client, "http://user-management:8080")

    // Helper function to extract the auth token
    suspend fun PipelineContext<Unit, ApplicationCall>.extractAuthToken(): String? {
        return call.request.headers[HttpHeaders.Authorization]?.takeIf { it.isNotBlank() }
            ?: run {
                call.respond(HttpStatusCode.Unauthorized, "Authorization token missing")
                null
            }
    }

    // Helper function to check permissions
    suspend fun PipelineContext<Unit, ApplicationCall>.checkPermission(
        authToken: String,
        permission: Permission
    ): String? {
        val userInfo = userService.getPermissions(authToken, permission.name)
        return if (userInfo.hasPermission) userInfo.userId else {
            call.respond(HttpStatusCode.Unauthorized, "Insufficient permissions")
            null
        }
    }

    route("/tasks") {
        get {
            val tasks = taskService.getAllTasks()
                .map { it.toResponse() }
            call.respond(tasks)
        }
        get("/byUser") {
            val authToken = extractAuthToken() ?: return@get
            val userId = checkPermission(authToken, Permission.VIEW) ?: return@get

            val tasks = taskService.getAllTasksForUser(ObjectId(userId))
                .map { it.toResponse() }
            call.respond(tasks)
        }

        get("/{id?}") {
            try {
                val authToken = extractAuthToken() ?: return@get
                checkPermission(authToken, Permission.VIEW) ?: return@get

                val id = call.parameters["id"]
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing id")

                taskService.getTaskById(ObjectId(id))?.let {
                    call.respond(it.toResponse())
                } ?: call.respond(HttpStatusCode.NotFound, "No records found for id $id")
            } catch (ex: Exception) {
                call.respond(HttpStatusCode.BadRequest, "An error occurred")
            }
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

        // Create a task
        post {
            try {
                val authToken = extractAuthToken() ?: return@post
                val userId = checkPermission(authToken, Permission.CREATE) ?: return@post

                val task = call.receive<TaskRequest>()
                val insertedId = taskService.createTask(task.toDomain(userId))

                call.respond(HttpStatusCode.Created, "Created task with id $insertedId")
            } catch (ex: Exception) {
                call.respond(HttpStatusCode.BadRequest, "An error occurred")
            }
        }

        // Delete a task
        delete("/{id?}") {
            try {
                val authToken = extractAuthToken() ?: return@delete
                checkPermission(authToken, Permission.DELETE) ?: return@delete

                val id = call.parameters["id"]
                    ?: return@delete call.respond(HttpStatusCode.BadRequest, "Missing id")

                if (taskService.deleteTask(ObjectId(id))) {
                    call.respond(HttpStatusCode.OK, "Task Deleted successfully")
                }

                call.respond(HttpStatusCode.NotFound, "Task not found")
            } catch (ex: Exception) {
                call.respond(HttpStatusCode.BadRequest, "An error occurred")
            }
        }

        // Update a task
        patch("/{id?}") {
            try {
                val authToken = extractAuthToken() ?: return@patch
                val userId = checkPermission(authToken, Permission.CREATE) ?: return@patch

                val id = call.parameters["id"]
                    ?: return@patch call.respond(HttpStatusCode.BadRequest, "Missing task id")
                val task = call.receive<TaskUpdate>()
                val updated = taskService.updateTask(ObjectId(id), task.toDomain(userId))

                if (updated == 1L) {
                    call.respond(HttpStatusCode.OK, "Task updated successfully")
                } else {
                    call.respond(HttpStatusCode.NotFound, "Task not found")
                }
            } catch (ex: Exception) {
                call.respond(HttpStatusCode.BadRequest, "An error occurred")
            }
        }
    }
}