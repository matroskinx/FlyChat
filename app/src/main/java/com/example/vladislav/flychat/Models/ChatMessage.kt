package com.example.vladislav.flychat.Models

data class ChatMessage(val id: String, val body: String, val fromUid: String, val toUid: String, val timestamp: Long)