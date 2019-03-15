package com.example.vladislav.flychat.Conversation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vladislav.flychat.Models.ChatMessage

class MessagesViewModel : ViewModel() {
    private val messages: ArrayList<ChatMessage> = ArrayList()

    fun getMessages(): ArrayList<ChatMessage> {
        return messages
    }
}