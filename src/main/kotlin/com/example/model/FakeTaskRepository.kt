package com.example.model

import Priority
import Task

class FakeTaskRepository : TaskRepository {
    private val tasks = mutableListOf(
        Task("cleaning", "Clean the house", Priority.Low),
        Task("gardening", "Mow the lawn", Priority.Medium),
        Task("shopping", "Buy the groceries", Priority.High),
        Task("painting", "Paint the fence", Priority.Medium)
    )

    override suspend fun allTasks(): List<Task> = tasks

    override suspend fun tasksByPriority(priority: Priority): List<Task> = tasks.filter { it.priority == priority }

    override suspend fun taskByName(name: String): Task? {
        return tasks.find {
            it.name.equals(name, ignoreCase = true)
        }
    }

    override suspend fun addTask(task: Task) {
        check(taskByName(task.name) == null) { "Cannot duplicate task names!" }

        tasks.add(task)
    }

    override suspend fun removeTask(name: String): Boolean {
        TODO("Not yet implemented")
    }
}