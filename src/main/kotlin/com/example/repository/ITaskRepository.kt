package com.example.repository

import com.example.entities.Priority
import com.example.entities.Task
import org.bson.BsonValue
import org.bson.types.ObjectId

interface ITaskRepository {
    suspend fun allTasks(): List<Task>
    suspend fun addTask(task: Task): BsonValue?
    suspend fun updateTask(id: ObjectId, task: Task): Long?
    suspend fun removeTask(id: ObjectId): Boolean
    suspend fun findById(id: ObjectId): Task?
    suspend fun findByPriority(priority: Priority): List<Task>
    suspend fun findByName(name: String): Task?
}