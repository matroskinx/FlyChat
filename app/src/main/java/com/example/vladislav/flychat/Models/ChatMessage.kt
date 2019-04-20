package com.example.vladislav.flychat.Models

data class ChatMessage(
    val id: String,
    val text: String,
    val userName: String,
    val timestamp: Long
)