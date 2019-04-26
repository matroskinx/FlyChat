package com.example.vladislav.flychat.Conversation

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vladislav.flychat.Models.ChatMessage
import com.example.vladislav.flychat.R
import com.example.vladislav.flychat.inflate
import kotlinx.android.synthetic.main.recyclerview_chat_left.view.*
import kotlinx.android.synthetic.main.recyclerview_chat_right.view.*
import java.lang.IllegalStateException

class ConversationRecyclerAdapter(private val messageList: MutableList<ChatMessage>, private val currentUid: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when(viewType) {
            LEFT_CHAT -> {
                val inflatedView = parent.inflate(R.layout.recyclerview_chat_right, false)
                return RightMessageHolder(inflatedView)
            }
            RIGHT_CHAT -> {
                val inflatedView = parent.inflate(R.layout.recyclerview_chat_left, false)
                return LeftMessageHolder(inflatedView)
            }
            else -> throw IllegalStateException("No such itemView type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(messageList[position].userName) {
            currentUid -> LEFT_CHAT
            else -> RIGHT_CHAT
        }
    }

    override fun getItemCount() = messageList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType) {
            LEFT_CHAT -> {
                val item = messageList[position]
                (holder as RightMessageHolder).bindMessage(item)
            }
            RIGHT_CHAT -> {
                val item = messageList[position]
                (holder as LeftMessageHolder).bindMessage(item)
            }
        }
    }


    class RightMessageHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val view: View = v
        private var message: ChatMessage? = null

        fun bindMessage(chatMessage: ChatMessage) {
            this.message = chatMessage
            view.mes_right.text = chatMessage.text
            view.mes_right_username.text = chatMessage.userName
        }
    }

    class LeftMessageHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val view: View = v
        private var message: ChatMessage? = null

        fun bindMessage(chatMessage: ChatMessage) {
            this.message = chatMessage
            view.mes_left.text = chatMessage.text
            view.mes_left_username.text = chatMessage.userName
        }
    }

    companion object {
        const val LEFT_CHAT = 0
        const val RIGHT_CHAT = 1
    }
}