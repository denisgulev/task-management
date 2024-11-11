package com.example.entities

enum class Permission(val permission: String) {
    ALL("all"),
    VIEW("view"),
    CREATE("create"),
    DELETE("delete")
}

data class UserInfo(
    val hasPermission: Boolean,
    val userId: String
)