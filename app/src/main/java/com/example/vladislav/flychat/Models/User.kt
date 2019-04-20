package com.example.vladislav.flychat.Models

data class User(
    val uid: String = "",
    val chats: MutableList<String> = mutableListOf(),
    val email: String = "",
    val username: String = "",
    val profileImageURL: String = ""
)