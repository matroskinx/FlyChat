package com.example.vladislav.flychat.AllChats

import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.vladislav.flychat.Models.User
import com.example.vladislav.flychat.R
import com.example.vladislav.flychat.inflate
import kotlinx.android.synthetic.main.recyclerview_user_row.view.*

class UserListRecyclerAdapter(private val users: MutableList<User>, private val userClickListener: UserClickListener) :
    RecyclerView.Adapter<UserListRecyclerAdapter.UserHolder>() {

    interface UserClickListener {
        fun userClicked(uid: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val inflatedView = parent.inflate(R.layout.recyclerview_user_row, false)
        return UserHolder(inflatedView, userClickListener)
    }

    override fun getItemCount() = users.size

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        val itemUser = users[position]
        holder.bindUser(itemUser)
    }

    class UserHolder(v: View, private val listener: UserClickListener) : RecyclerView.ViewHolder(v),
        View.OnClickListener {

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            //TODO pass chatId not userId
            listener.userClicked(user!!.uid)
            //findNavController(view).navigate(NewChatFragmentDirections.actionNewChatFragmentToConversationFragment(user!!.uid))
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