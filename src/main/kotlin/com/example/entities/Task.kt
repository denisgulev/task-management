package com.example.entities

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Task(
    @BsonId
    val id: ObjectId,
    val name: String,
    val description: String,
    val priority: Priority
)

enum class Priority {
    Low, Medium, High, Vital
}