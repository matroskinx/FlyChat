package com.example.vladislav.flychat.Conversation


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vladislav.flychat.Repository.AllChatsRemoteRepository
import com.example.vladislav.flychat.Models.ChatMessage
import com.example.vladislav.flychat.R
import com.example.vladislav.flychat.Register.RegisterActivity
import com.example.vladislav.flychat.Repository.PicturesRemoteRepository
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
            rv_messages.scrollToPosition(it.size - 1)
        }

        repo.messageList.observe(this, messageListObserver)

        sms.setOnClickListener {
            val text = input_text.text.toString()
            input_text.text.clear()
            repo.sendMessage(text, args.chatId)
        }

        attach_btn.setOnClickListener {
            dispatchPickPictureIntent()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 23 && resultCode == Activity.RESULT_OK && data != null) {
            val uri = data.data
            PicturesRemoteRepository().uploadPicture(uri.toString(), repo.newChatId.value as String)
        }
    }

    private fun dispatchPickPictureIntent() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 23)
    }
}
