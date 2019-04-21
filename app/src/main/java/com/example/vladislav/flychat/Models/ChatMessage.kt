package com.example.vladislav.flychat.Models

data class ChatMessage(
    val text: String = "",
    val timestamp: Long = 0,
    val userName: String = ""
)