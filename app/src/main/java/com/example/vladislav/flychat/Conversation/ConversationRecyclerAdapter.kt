package com.example.vladislav.flychat.Conversation

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vladislav.flychat.DateUtility
import com.example.vladislav.flychat.Models.ChatMessage
import com.example.vladislav.flychat.PictureActivity
import com.example.vladislav.flychat.R
import com.example.vladislav.flychat.inflate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recyclerview_chat_left.view.*
import kotlinx.android.synthetic.main.recyclerview_chat_right.view.*
import kotlinx.android.synthetic.main.recyclerview_image_row.view.*
import kotlinx.android.synthetic.main.recyclerview_image_row_left.view.*
import java.lang.IllegalStateException

class ConversationRecyclerAdapter(private val messageList: MutableList<ChatMessage>, private val currentUid: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when (viewType) {
            RIGHT_CHAT -> {
                val inflatedView = parent.inflate(R.layout.recyclerview_chat_right, false)
                return RightMessageHolder(inflatedView)
            }
            LEFT_CHAT -> {
                val inflatedView = parent.inflate(R.layout.recyclerview_chat_left, false)
                return LeftMessageHolder(inflatedView)
            }
            IMAGE_CHAT -> {
                val inflatedView = parent.inflate(R.layout.recyclerview_image_row, false)
                return ImageHolder(inflatedView)
            }
            IMAGE_CHAT_LEFT -> {
                val inflatedView = parent.inflate(R.layout.recyclerview_image_row_left, false)
                return LeftImageHolder(inflatedView)
            }
            else -> throw IllegalStateException("No such itemView type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (messageList[position].fromId) {
            currentUid -> {
                messageList[position].imageUrl?.let {
                    return IMAGE_CHAT
                }
                RIGHT_CHAT
            }
            else -> {
                messageList[position].imageUrl?.let {
                    return IMAGE_CHAT_LEFT
                }
                LEFT_CHAT
            }
        }

    }

    override fun getItemCount() = messageList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            RIGHT_CHAT -> {
                val item = messageList[position]
                (holder as RightMessageHolder).bindMessage(item)
            }
            LEFT_CHAT -> {
                val item = messageList[position]
                (holder as LeftMessageHolder).bindMessage(item)
            }
            IMAGE_CHAT -> {
                val item = messageList[position]
                (holder as ImageHolder).bindMessage(item)
            }
            IMAGE_CHAT_LEFT -> {
                val item = messageList[position]
                (holder as LeftImageHolder).bindMessage(item)
            }
        }
    }


    class RightMessageHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val view: View = v
        private var message: ChatMessage? = null

        fun bindMessage(chatMessage: ChatMessage) {
            this.message = chatMessage
            view.mes_right.text = DateUtility.getDate(chatMessage.time)
            //view.mes_right_username.text = chatMessage.text
            view.mes_right_username.text = chatMessage.text
        }
    }

    class LeftMessageHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val view: View = v
        private var message: ChatMessage? = null

        fun bindMessage(chatMessage: ChatMessage) {
            this.message = chatMessage
            view.mes_left.text = DateUtility.getDate(chatMessage.time)
            //view.mes_left_username.text = chatMessage.fromId
            view.mes_left_username.text = chatMessage.text
        }
    }

    class ImageHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private val view: View = v
        private var message: ChatMessage? = null

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val context = view.context
            val intent = Intent(context, PictureActivity::class.java)
            intent.putExtra(URL_KEY, this.message!!.imageUrl)
            context.startActivity(intent)
        }

        fun bindMessage(chatMessage: ChatMessage) {
            this.message = chatMessage
            Picasso.get()
                .load(message?.imageUrl)
                .fit()
                .into(view.row_image)

            view.image_time.text = DateUtility.getDate(chatMessage.time)
        }
    }

    class LeftImageHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private val view: View = v
        private var message: ChatMessage? = null

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val context = view.context
            val intent = Intent(context, PictureActivity::class.java)
            intent.putExtra(URL_KEY, this.message!!.imageUrl)
            context.startActivity(intent)
        }

        fun bindMessage(chatMessage: ChatMessage) {
            this.message = chatMessage
            Picasso.get()
                .load(message?.imageUrl)
                .fit()
                .into(view.row_image_left)

            view.image_time_left.text = DateUtility.getDate(chatMessage.time)
        }
    }

    companion object {
        const val RIGHT_CHAT = 0
        const val LEFT_CHAT = 1
        const val IMAGE_CHAT = 2
        const val IMAGE_CHAT_LEFT = 3
        const val URL_KEY = "pic_url"
    }
}