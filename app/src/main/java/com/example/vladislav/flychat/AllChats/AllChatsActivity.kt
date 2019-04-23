package com.example.vladislav.flychat.AllChats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vladislav.flychat.Models.ChatMessage
import com.example.vladislav.flychat.R
import java.util.*

class AllChatsActivity : AppCompatActivity() {

    private fun timestampSelector(message: ChatMessage): Long = message.timestamp
    private var messageList: ArrayList<ChatMessage> = ArrayList()
    private lateinit var adapterLatestMessages: LatestMessagesRecyclerAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)
    }
}
