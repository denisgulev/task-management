package com.example.entities

enum class Permission(val permission: String) {
    ALL("all"),
    VIEW("view"),
    CREATE("create"),
    DELETE("delete")
}