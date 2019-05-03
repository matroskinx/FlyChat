package com.example.vladislav.flychat.Utilities

import java.text.SimpleDateFormat
import java.util.*

object DateUtility {
    fun getDate(timestamp: Long): String {
        val dateTimestamp = timestamp * 1000   // time back to millis
        val currentDate = System.currentTimeMillis()
        val date = Date(dateTimestamp)
        val dateDiff = currentDate - dateTimestamp

        return when {
            dateDiff < 1000 * 60 * 60 * 24 -> {
                val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                sdf.format(date)
            }
            dateDiff < 1000 * 60 * 60 * 24 * 7 -> {
                val sdf = SimpleDateFormat("E", Locale.getDefault())
                sdf.format(date)
            }
            else -> {
                val sdf = SimpleDateFormat("yyyy", Locale.getDefault())
                sdf.format(date)
            }
        }
    }
}