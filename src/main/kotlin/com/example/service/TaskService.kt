package com.example.service

import com.example.entities.Priority
import com.example.entities.Task
import com.example.repository.ITaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.types.ObjectId
import org.koin.core.annotation.Single

@Single
class TaskService(
    private val repository: ITaskRepository
) : ITaskService {
    override suspend fun getAllTasks(): List<Task> = withContext(Dispatchers.IO) {
        repository.allTasks()
    }

    override suspend fun getAllTasksForUser(userId: ObjectId): List<Task> = withContext(Dispatchers.IO) {
        repository.allTasksForUser(userId)
    }

    override suspend fun getTaskById(id: ObjectId): Task? = withContext(Dispatchers.IO) {
        repository.findById(id)
    }

    override suspend fun getTaskByName(name: String): Task? = withContext(Dispatchers.IO) {
        repository.findByName(name)
    }

    override suspend fun getTasksByPriority(priority: Priority): List<Task> = withContext(Dispatchers.IO) {
        repository.findByPriority(priority)
    }

    override suspend fun createTask(task: Task): String = withContext(Dispatchers.IO) {
        repository.addTask(task).toString()
    }

    override suspend fun deleteTask(id: ObjectId): Boolean = withContext(Dispatchers.IO) {
        repository.removeTask(id)
    }

    override suspend fun updateTask(id: ObjectId, task: Task): Long? = withContext(Dispatchers.IO) {
        repository.updateTask(id, task)
    }

}