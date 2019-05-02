package com.example.vladislav.flychat.AllChats


import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vladislav.flychat.Models.User

import com.example.vladislav.flychat.R
import kotlinx.android.synthetic.main.fragment_new_chat.*

class NewChatFragment : Fragment() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapterUsers: UserListRecyclerAdapter
//    private var repo = AllChatsRemoteRepository()
    private lateinit var viewModel: AllChatsViewModel

    private val userClickListener = object : UserListRecyclerAdapter.UserClickListener {
        override fun userClicked(uid: String) {
            viewModel.remoteRepository.startNewChat(uid)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            viewModel = ViewModelProviders.of(it).get(AllChatsViewModel::class.java)
        }
        viewModel.remoteRepository.getAvailableUsers()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        linearLayoutManager = LinearLayoutManager(activity)
        rv_all_users.layoutManager = linearLayoutManager
        rv_all_users.setHasFixedSize(true)

        val usersObserver = Observer<MutableMap<String, User>> {
            adapterUsers = UserListRecyclerAdapter(it.values.toMutableList(), userClickListener)
            rv_all_users.adapter = adapterUsers
        }

        val newChatCreatedObserver = Observer<String> { chatId ->
            findNavController().navigate(NewChatFragmentDirections.actionNewChatFragmentToConversationFragment(chatId))
            Log.d("NENWNWWW", "obs triggered (newchat)")
        }

        viewModel.remoteRepository.userList.observe(this, usersObserver)

        viewModel.remoteRepository.newChatId.observe(this, newChatCreatedObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        //viewModel.remoteRepository.newChatId.removeObservers(this)
    }
}
