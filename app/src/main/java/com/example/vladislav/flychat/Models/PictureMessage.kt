package com.example.vladislav.flychat.Models

data class PictureMessage (
    val text: String = "",
    val time: Long = 0,
    val fromId: String = "",
    val pictureUrl: String? = null,
    val width: Int = 0,
    val height: Int = 0
)