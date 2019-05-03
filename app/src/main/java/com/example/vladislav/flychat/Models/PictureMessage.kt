package com.example.vladislav.flychat.Models

data class PictureMessage (
    val time: Long = 0,
    val fromId: String = "",
    val imageUrl: String? = null,
    val width: Int = 0,
    val height: Int = 0
)