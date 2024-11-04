package com.example.entities

import kotlinx.serialization.Serializable

@Serializable
data class TaskRequest(
    val name: String,
    val description: String,
    val priority: Priority
)