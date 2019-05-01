package com.example.vladislav.flychat.Models

data class ChatMessage(
    val text: String = "",
    val time: Long = 0,
    val fromId: String = "",
    val pictureUrl: String? = null
)