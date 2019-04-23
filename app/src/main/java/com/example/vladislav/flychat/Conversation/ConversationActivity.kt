package com.example.vladislav.flychat.Conversation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.vladislav.flychat.R
import kotlinx.android.synthetic.main.activity_conversation.*

class ConversationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)

        val messageFromId = intent.getStringExtra(UID_KEY)
        fromId_text.text = "recieved message from: $messageFromId"
    }

    companion object {
        private const val UID_KEY = "KEY92"
    }
}
