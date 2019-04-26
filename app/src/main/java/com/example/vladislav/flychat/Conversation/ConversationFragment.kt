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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repo.openChat(args.chatId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Toast.makeText(activity, args.chatId, Toast.LENGTH_LONG).show()
        return inflater.inflate(R.layout.fragment_conversation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        linearLayoutManager = LinearLayoutManager(activity)
        rv_messages.layoutManager = linearLayoutManager

        val messageListObserver = Observer<MutableList<ChatMessage>> {
            adapterMessages = ConversationRecyclerAdapter(it, repo.uid)
            rv_messages.adapter = adapterMessages
            Toast.makeText(activity, "Observer triggered", Toast.LENGTH_LONG).show()
            rv_messages.scrollToPosition(it.size -1)
        }

        repo.messageList.observe(this, messageListObserver)

        sms.setOnClickListener {
            val text = input_text.text.toString()
            repo.sendMessage(text, args.chatId)
        }
    }
}
