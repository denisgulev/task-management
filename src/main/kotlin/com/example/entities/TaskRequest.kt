package com.example.entities

import kotlinx.serialization.Serializable

@Serializable
data class TaskRequest @JvmOverloads constructor(
    val name: String,
    val description: String,
    val priority: Priority,
    val userId: String? = null
)

@Serializable
data class TaskUpdate(
    val id: String,
    val name: String,
    val description: String,
    val priority: Priority
)