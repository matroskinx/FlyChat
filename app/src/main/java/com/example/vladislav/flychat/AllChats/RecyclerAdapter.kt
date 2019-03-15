package com.example.vladislav.flychat.AllChats

import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vladislav.flychat.Conversation.ConversationActivity
import com.example.vladislav.flychat.Models.ChatMessage
import com.example.vladislav.flychat.R
import com.example.vladislav.flychat.inflate
import kotlinx.android.synthetic.main.recyclerview_chat_row.view.*

class RecyclerAdapter(private val messages: ArrayList<ChatMessage>) :
    RecyclerView.Adapter<RecyclerAdapter.MessageHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        val inflatedView = parent.inflate(R.layout.recyclerview_chat_row, false)
        return MessageHolder(inflatedView)
    }

    override fun getItemCount() = messages.size

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        val itemMessage = messages[position]
        holder.bindMessage(itemMessage)
    }

    class MessageHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v
        private var message: ChatMessage? = null

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val context = itemView.context
            val openConversationIntent = Intent(context, ConversationActivity::class.java)
            openConversationIntent.putExtra(USER_KEY, message?.fromUid)
            context.startActivity(openConversationIntent)
            Log.d("MESSAGESRV", "Click!")
        }

        fun bindMessage(message: ChatMessage) {
            this.message = message
            view.sender_message_text.text = message.body
            view.sender_name_text.text = message.fromUid
            view.sender_image.setImageResource(R.drawable.abc_ic_star_black_48dp)
        }

        companion object {
            private const val USER_KEY = "USER"
        }
    }
}