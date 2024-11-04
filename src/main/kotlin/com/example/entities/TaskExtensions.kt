package com.example.entities

import org.bson.types.ObjectId

fun Task.toResponse() = TaskResponse(
    id = id.toString(),
    name = name,
    description = description,
    priority = priority
)

fun TaskRequest.toDomain() = Task(
    id = ObjectId(),
    name = name,
    description = description,
    priority = priority
)