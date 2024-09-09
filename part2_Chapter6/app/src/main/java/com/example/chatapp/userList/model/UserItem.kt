package com.example.chatapp.userList.model

// Firebase DB에 연동
data class UserItem(
    val userId: String ?= null,
    val username: String ?= null,
    val description: String ?= null,
)
