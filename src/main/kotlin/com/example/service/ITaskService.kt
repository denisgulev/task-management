package com.example.service

import com.example.entities.Priority
import com.example.entities.Task
import org.bson.types.ObjectId

interface ITaskService {
    suspend fun getAllTasks(): List<Task>

    suspend fun getAllTasksForUser(userId: ObjectId): List<Task>

    suspend fun getTaskById(id: ObjectId): Task?

    suspend fun getTaskByName(name: String): Task?

    suspend fun getTasksByPriority(priority: Priority): List<Task>

    suspend fun createTask(task: Task): String

    suspend fun deleteTask(id: ObjectId): Boolean

    suspend fun updateTask(id: ObjectId, task: Task): Long?

}