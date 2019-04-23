package com.example.vladislav.flychat.AllChats

import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vladislav.flychat.Conversation.ConversationActivity
import com.example.vladislav.flychat.Models.User
import com.example.vladislav.flychat.R
import com.example.vladislav.flychat.inflate
import kotlinx.android.synthetic.main.recyclerview_user_row.view.*

class UserListRecyclerAdapter(private val users: MutableList<User>) :
    RecyclerView.Adapter<UserListRecyclerAdapter.UserHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val inflatedView = parent.inflate(R.layout.recyclerview_user_row, false)
        return UserHolder(inflatedView)
    }

    override fun getItemCount() = users.size

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        val itemUser = users[position]
        holder.bindUser(itemUser)
    }

    class UserHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        init {
            v.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            //val position = layoutPosition
            val context = itemView.context
            val openConversationIntent = Intent(context, ConversationActivity::class.java)
            openConversationIntent.putExtra(UID_KEY, user!!.uid)
            context.startActivity(openConversationIntent)
            Log.d("USERLISTRV", "User ${user!!.uid} opened")
        }

        private var view: View = v
        private var user: User? = null

        fun bindUser(user: User) {
            this.user = user
            view.user_row_email.text = user.email
            view.user_row_name.text = user.username
        }

        companion object {
            private const val UID_KEY = "KEY92"
        }
    }
}