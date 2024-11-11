package com.example.entities

import org.bson.types.ObjectId

fun Task.toResponse() = TaskResponse(
    id = id.toString(),
    name = name,
    description = description,
    priority = priority,
    userId = userId.toString()
)

fun TaskRequest.toDomain(userId: String) = Task(
    id = ObjectId(),
    name = name,
    description = description,
    priority = priority,
    userId = ObjectId(userId)
)

fun TaskUpdate.toDomain(userId: String) = Task(
    id = ObjectId(id),
    name = name,
    description = description,
    priority = priority,
    userId = ObjectId(userId)
)