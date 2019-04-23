package com.example.vladislav.flychat.AllChats

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vladislav.flychat.Models.User
import com.example.vladislav.flychat.R
import com.example.vladislav.flychat.inflate
import kotlinx.android.synthetic.main.recyclerview_user_row.view.*

class UserListRecyclerAdapter(private val users: MutableList<User>) :
    RecyclerView.Adapter<UserListRecyclerAdapter.UserHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListRecyclerAdapter.UserHolder {
        val inflatedView = parent.inflate(R.layout.recyclerview_user_row, false)
        return UserHolder(inflatedView)
    }

    override fun getItemCount() = users.size

    override fun onBindViewHolder(holder: UserListRecyclerAdapter.UserHolder, position: Int) {
        val itemUser = users[position]
        holder.bindUser(itemUser)
    }

    class UserHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        private var user: User? = null

        fun bindUser(user: User) {
            this.user = user
            view.user_row_email.text = user.email
            view.user_row_name.text = user.username
        }
    }
}