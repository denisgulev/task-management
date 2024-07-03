package com.example.domain.entity

import com.example.application.response.TaskResponse
import com.example.domain.enum.Priority
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Task (
    @BsonId
    val id: ObjectId,
    val name: String,
    val description: String,
    val priority: Priority
    )

fun Task.toResponse() = TaskResponse(
    id = id.toString(),
    name = name,
    description = description,
    priority = priority
)