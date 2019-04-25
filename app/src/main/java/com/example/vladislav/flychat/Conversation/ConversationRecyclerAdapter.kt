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

class ConversationRecyclerAdapter(private val messageList: MutableList<ChatMessage>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when(viewType) {
            0 -> {
                val inflatedView = parent.inflate(R.layout.recyclerview_chat_right, false)
                return RightMessageHolder(inflatedView)
            }
            2 -> {
                val inflatedView = parent.inflate(R.layout.recyclerview_chat_left, false)
                return LeftMessageHolder(inflatedView)
            }
            else -> throw IllegalStateException("No such itemView type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position % 2 * 2
    }

    override fun getItemCount() = messageList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType) {
            0 -> {
                val item = messageList[position]
                (holder as RightMessageHolder).bindMessage(item)
            }
            2 -> {
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
}