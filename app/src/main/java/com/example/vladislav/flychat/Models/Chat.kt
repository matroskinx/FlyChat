package com.example.vladislav.flychat.Models

data class Chat(
    val id: String,
    val lastMessage: LastMessage,
    val messages: MutableList<ChatMessage>,
    val users: MutableList<String>
)