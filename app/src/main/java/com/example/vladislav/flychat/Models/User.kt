package com.example.vladislav.flychat.Models

data class User(
    val uid: String = "",
    val chats: MutableList<String> = mutableListOf(),
    val email: String = "",
    val name: String = "",
    val profileImageUrl: String = ""
)