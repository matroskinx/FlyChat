package com.example.vladislav.flychat.AllChats

import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.vladislav.flychat.Models.LastMessage
import com.example.vladislav.flychat.Models.User
import com.example.vladislav.flychat.R
import com.example.vladislav.flychat.inflate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recyclerview_chat_row.view.*
import java.text.SimpleDateFormat
import java.util.*


class LatestMessagesRecyclerAdapter(
    private val messages: MutableMap<String, LastMessage>,
    //private val userList: MutableMap<String, User>,
    private val pictureUrls: MutableMap<String, String> = mutableMapOf()
) :
    RecyclerView.Adapter<LatestMessagesRecyclerAdapter.MessageHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        val inflatedView = parent.inflate(R.layout.recyclerview_chat_row, false)
        return MessageHolder(inflatedView, messages)
    }

    override fun getItemCount() = messages.size

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        val itemMessage = messages.values.elementAt(position)
        val chatId = messages.keys.elementAt(position)
        val pictureUrl: String? = pictureUrls[chatId]

        holder.bindMessage(itemMessage, pictureUrl)
    }

    class MessageHolder(v: View, private val messages: MutableMap<String, LastMessage>) : RecyclerView.ViewHolder(v),
        View.OnClickListener {
        private var view: View = v
        private var message: LastMessage? = null

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val chatId = messages.keys.elementAt(adapterPosition)
            findNavController(v).navigate(AllChatsFragmentDirections.actionAllChatsFragmentToConversationFragment(chatId))
            Log.d("MESSAGESRV", "Click!")
        }

        fun bindMessage(message: LastMessage, pictureUrl: String?) {
            this.message = message
            //TODO fix display messages
            view.sender_message_text.text = message.text
            view.sender_name_text.text = message.text
            //view.sender_image.setImageResource(R.drawable.abc_ic_star_black_48dp)


            pictureUrl?.let {
                Picasso.get().load(it).into(view.sender_image)
            } ?: view.sender_image.setImageResource(R.drawable.abc_ic_star_black_48dp)


            val dateTimestamp = message.time * 1000   // time back to millis
            val currentDate = System.currentTimeMillis()
            val date = Date(dateTimestamp)
            val dateDiff = currentDate - dateTimestamp

            view.time_text.text = when {
                dateDiff < 1000 * 60 * 60 * 24 -> {
                    val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                    sdf.format(date)
                }
                dateDiff < 1000 * 60 * 60 * 24 * 7 -> {
                    val sdf = SimpleDateFormat("E", Locale.getDefault())
                    sdf.format(date)
                }
                else -> {
                    val dateFormat = android.text.format.DateFormat.getDateFormat(itemView.context)
                    dateFormat.format(date)
                }
            }
        }

        companion object {
            private const val USER_KEY = "USER"
        }
    }
}