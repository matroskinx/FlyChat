package com.example.vladislav.flychat.AllChats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vladislav.flychat.Conversation.MessagesViewModel
import com.example.vladislav.flychat.Models.ChatMessage
import com.example.vladislav.flychat.R
import kotlinx.android.synthetic.main.activity_chats.*
import java.util.*
import kotlin.random.Random

class AllChatsActivity : AppCompatActivity() {

    private fun timestampSelector(message: ChatMessage): Long = message.timestamp
    private var messageList: ArrayList<ChatMessage> = ArrayList()
    private lateinit var adapter: RecyclerAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    var counter = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)
        linearLayoutManager = LinearLayoutManager(this)
        chats_rc.layoutManager = linearLayoutManager

        val model = ViewModelProviders.of(this).get(MessagesViewModel::class.java)
        messageList = model.getMessages()

        adapter = RecyclerAdapter(messageList)
        chats_rc.adapter = adapter
        chats_rc.setHasFixedSize(true)

        button_test.setOnClickListener{
            AllChatsRemoteRepository().loadChatsList()
        }

/*        button_test.setOnClickListener {
            counter += 1
            val testMessage = ChatMessage(
                counter.toString(),
                "Random",
                "Vlad",
                "Test",
                System.currentTimeMillis() / 1000 + Random.nextInt(-1 * 60 * 60 * 24 * 14, 0)  // timestamp in seconds
            )

            messageList.add(testMessage)
            messageList.sortByDescending { timestampSelector(it) }
            val insertedItemIndex = messageList.indexOf(testMessage)
            adapter.notifyItemInserted(insertedItemIndex)

        }*/

    }
}
