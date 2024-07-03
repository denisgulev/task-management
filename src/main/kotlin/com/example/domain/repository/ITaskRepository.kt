package com.example.domain.repository

import com.example.domain.entity.Task
import com.example.domain.enum.Priority
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