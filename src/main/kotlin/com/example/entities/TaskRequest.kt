package com.example.entities

import kotlinx.serialization.Serializable

@Serializable
data class TaskRequest(
    val name: String,
    val description: String,
    val priority: Priority
)

@Serializable
data class TaskUpdate(
    val id: String,
    val name: String,
    val description: String,
    val priority: Priority
)