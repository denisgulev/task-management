package com.example.application.response

import com.example.domain.enum.Priority

data class TaskResponse(
    val id: String,
    val name: String,
    val description: String,
    val priority: Priority
)