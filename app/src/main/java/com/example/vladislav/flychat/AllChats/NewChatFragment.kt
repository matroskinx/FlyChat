package com.example.vladislav.flychat.AllChats


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.vladislav.flychat.R
import kotlinx.android.synthetic.main.fragment_new_chat.*

class NewChatFragment : Fragment() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: UserListRecyclerAdapter
    private var repo = AllChatsRemoteRepository()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        linearLayoutManager = LinearLayoutManager(activity)
        rv_all_users.layoutManager = linearLayoutManager
        rv_all_users.setHasFixedSize(true)

        repo.getAvailableUsers()

        load_users_btn.setOnClickListener{
            adapter = UserListRecyclerAdapter(repo.userList.values.toMutableList())
            rv_all_users.adapter = adapter
        }
    }
}
