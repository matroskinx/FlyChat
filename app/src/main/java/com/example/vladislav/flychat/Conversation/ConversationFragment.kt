package com.example.vladislav.flychat.Conversation


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vladislav.flychat.AllChats.AllChatsRemoteRepository
import com.example.vladislav.flychat.R
import kotlinx.android.synthetic.main.fragment_conversation.*
import kotlin.random.Random


class ConversationFragment : Fragment() {

    private val args: ConversationFragmentArgs by navArgs()
    private lateinit var adapterMessages: ConversationRecyclerAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val repo = AllChatsRemoteRepository()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Toast.makeText(activity, args.chatId, Toast.LENGTH_LONG).show()
        return inflater.inflate(R.layout.fragment_conversation, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repo.openChat("c7f78c50-dfa3-4e6d-a154-6ebfb398359a")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        load_messages_btn.setOnClickListener {
            linearLayoutManager = LinearLayoutManager(activity)
            rv_messages.layoutManager = linearLayoutManager
            adapterMessages = ConversationRecyclerAdapter(repo.messageList)
            rv_messages.adapter = adapterMessages
            rv_messages.setHasFixedSize(true)
        }
    }
}
