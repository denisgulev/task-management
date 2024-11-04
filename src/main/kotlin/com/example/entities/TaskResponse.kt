package com.example.entities

import kotlinx.serialization.Serializable

@Serializable
data class TaskResponse(
    val id: String,
    val name: String,
    val description: String,
    val priority: Priority
)