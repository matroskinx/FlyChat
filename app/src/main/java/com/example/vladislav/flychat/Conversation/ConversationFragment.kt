package com.example.vladislav.flychat.Conversation


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vladislav.flychat.AllChats.AllChatsRemoteRepository
import com.example.vladislav.flychat.Models.ChatMessage
import com.example.vladislav.flychat.R
import kotlinx.android.synthetic.main.fragment_conversation.*


class ConversationFragment : Fragment() {

    private val args: ConversationFragmentArgs by navArgs()
    private lateinit var adapterMessages: ConversationRecyclerAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val repo = AllChatsRemoteRepository()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Toast.makeText(activity, args.chatId, Toast.LENGTH_LONG).show()
        return inflater.inflate(R.layout.fragment_conversation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        linearLayoutManager = LinearLayoutManager(activity)
        rv_messages.layoutManager = linearLayoutManager

        val messageListObserver = Observer<MutableList<ChatMessage>> {
            adapterMessages = ConversationRecyclerAdapter(it)
            rv_messages.adapter = adapterMessages
            Toast.makeText(activity, "Observer triggered", Toast.LENGTH_LONG).show()
        }

        repo.messageList.observe(this, messageListObserver)

        load_messages_btn.setOnClickListener {
            repo.openChat("c7f78c50-dfa3-4e6d-a154-6ebfb398359a")
        }

        sms.setOnClickListener {
            repo.sendMessage("It's updated!", "c7f78c50-dfa3-4e6d-a154-6ebfb398359a")
        }
    }
}
