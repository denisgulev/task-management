package com.example.application.request

import com.example.domain.entity.Task
import com.example.domain.enum.Priority
import org.bson.types.ObjectId

data class TaskRequest (
    val name: String,
    val description: String,
    val priority: Priority
)

fun TaskRequest.toDomain(): Task {
    return Task(
        id = ObjectId(),
        name = name,
        description = description,
        priority = priority
    )
}