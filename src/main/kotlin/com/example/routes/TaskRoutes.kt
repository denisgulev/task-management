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
import org.bson.types.ObjectId
import org.koin.ktor.ext.inject

fun Route.taskRoutes(client: HttpClient) {
    val taskService by inject<ITaskService>()
    val userService = UserService(client, "http://user-management:8080")

    route("/tasks") {
        get {
            val tasks = taskService.getAllTasks()
                .map { it.toResponse() }
            call.respond(tasks)
        }

        get("/{id?}") {
            try {
                val authToken = call.request.headers.getAll(HttpHeaders.Authorization)?.firstOrNull()
                    ?: return@get call.respond(HttpStatusCode.Unauthorized)

                // Step 1: Check user permission from user service
                val userHasPermission = userService.getPermissions(authToken, Permission.VIEW.name)
                if (!userHasPermission) {
                    return@get call.respond(HttpStatusCode.Unauthorized)
                }
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
                val authToken = call.request.headers.getAll(HttpHeaders.Authorization)?.firstOrNull()
                    ?: return@post call.respond(HttpStatusCode.Unauthorized)

                // Step 1: Check user permission from user service
                val userHasPermission = userService.getPermissions(authToken, Permission.CREATE.name)
                if (!userHasPermission) {
                    return@post call.respond(HttpStatusCode.Unauthorized)
                }

                // Step 2: Extract user ID from verified user info
//                val userId = userInfo.id

                // Step 3: Create task and associate with user ID
                val task = call.receive<TaskRequest>()
                val insertedId = taskService.createTask(task.toDomain())
                call.respond(HttpStatusCode.Created, "Created task with id $insertedId")
            } catch (ex: Exception) {
                call.respond(HttpStatusCode.BadRequest, "An error occurred")
            }
        }

        // Delete a task
        delete("/{id?}") {
            try {
                val authToken = call.request.headers.getAll(HttpHeaders.Authorization)?.firstOrNull()
                    ?: return@delete call.respond(HttpStatusCode.Unauthorized)

                // Check user permission for DELETE
                val userHasPermission = userService.getPermissions(authToken, Permission.DELETE.name)
                if (!userHasPermission) {
                    return@delete call.respond(HttpStatusCode.Unauthorized)
                }
                val id = call.parameters["id"]
                    ?: return@delete call.respond(HttpStatusCode.BadRequest, "Missing id")

                val deleted = taskService.deleteTask(ObjectId(id))
                if (deleted) {
                    return@delete call.respond(HttpStatusCode.OK, "Task Deleted successfully")
                }

                return@delete call.respond(HttpStatusCode.NotFound, "Task not found")
            } catch (ex: Exception) {
                call.respond(HttpStatusCode.BadRequest, "An error occurred")
            }
        }

        // Update a task
        patch("/{id?}") {
            try {
                val authToken = call.request.headers.getAll(HttpHeaders.Authorization)?.firstOrNull()
                    ?: return@patch call.respond(HttpStatusCode.Unauthorized)

                // Check user permission for UPDATE
                val userHasPermission = userService.getPermissions(authToken, Permission.CREATE.name)
                if (!userHasPermission) {
                    return@patch call.respond(HttpStatusCode.Unauthorized)
                }
                val id = call.parameters["id"]
                    ?: return@patch call.respond(HttpStatusCode.BadRequest, "Missing task id")
                val task = call.receive<TaskUpdate>()
                val updated = taskService.updateTask(ObjectId(id), task.toDomain())

                return@patch if (updated == 1L) {
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