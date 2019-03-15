package com.example.vladislav.flychat.AllChats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vladislav.flychat.Models.ChatMessage
import com.example.vladislav.flychat.R
import kotlinx.android.synthetic.main.activity_chats.*

class AllChatsActivity : AppCompatActivity() {

    private var messageList: ArrayList<ChatMessage> = ArrayList()
    private lateinit var adapter: RecyclerAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    var counter = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)
        linearLayoutManager = LinearLayoutManager(this)
        chats_rc.layoutManager = linearLayoutManager

        val a = ChatMessage("1", "Hello", "Vlad", "Test", 5040)
        val b = ChatMessage("2", "It's me", "Vlad", "Test", 5050)
        val c = ChatMessage("3", "Darkness", "Vlad", "Test", 5060)

        messageList.addAll(listOf(a, b, c))

        adapter = RecyclerAdapter(messageList)
        chats_rc.adapter = adapter
        chats_rc.setHasFixedSize(true)

        button_test.setOnClickListener {
            counter += 1
            messageList.add(
                ChatMessage(
                    counter.toString(),
                    "Random",
                    "Vlad",
                    "Test",
                    5070
                )
            )
            adapter.notifyItemInserted(messageList.size)

        }

    }


}
