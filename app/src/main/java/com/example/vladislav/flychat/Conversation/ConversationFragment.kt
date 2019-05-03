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
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vladislav.flychat.AllChats.AllChatsViewModel
import com.example.vladislav.flychat.Repository.AllChatsRemoteRepository
import com.example.vladislav.flychat.Models.ChatMessage
import com.example.vladislav.flychat.R
import com.example.vladislav.flychat.Register.RegisterActivity
import com.example.vladislav.flychat.Repository.PicturesRemoteRepository
import kotlinx.android.synthetic.main.fragment_conversation.*
import android.graphics.BitmapFactory




class ConversationFragment : Fragment() {

    private val args: ConversationFragmentArgs by navArgs()
    private lateinit var adapterMessages: ConversationRecyclerAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var viewModel: AllChatsViewModel

    private val onUploadSuccessListener = object : PicturesRemoteRepository.OnUploadResult {
        override fun onUploadSuccess(downloadLink: String, width: Int, height: Int) {
            //viewModel.remoteRepository.sendMessage("picture", args.chatId, downloadLink)
            viewModel.remoteRepository.sendPicture("", args.chatId, downloadLink, width, height )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            viewModel = ViewModelProviders.of(it).get(AllChatsViewModel::class.java)
        }
        viewModel.remoteRepository.openChat(args.chatId)
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
            adapterMessages = ConversationRecyclerAdapter(it, viewModel.remoteRepository.uid)
            rv_messages.adapter = adapterMessages
            Toast.makeText(activity, "Observer triggered", Toast.LENGTH_LONG).show()
            rv_messages.scrollToPosition(it.size - 1)
        }

        viewModel.remoteRepository.messageList.observe(this, messageListObserver)

        sms.setOnClickListener {
            val text = input_text.text.toString()
            input_text.text.clear()
            if(text.isNotEmpty()) {
                viewModel.remoteRepository.sendMessage(text, args.chatId)
            }
        }

        attach_btn.setOnClickListener {
            dispatchPickPictureIntent()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 23 && resultCode == Activity.RESULT_OK && data != null) {
            val uri = data.data

            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            BitmapFactory.decodeStream( context?.contentResolver?.openInputStream(uri), null, o)

            val pictureWidth = o.outWidth
            val pictureHeight = o.outHeight

            PicturesRemoteRepository().uploadPicture(uri.toString(), pictureWidth, pictureHeight, args.chatId, onUploadSuccessListener)
        }
    }

    private fun dispatchPickPictureIntent() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 23)
    }
}
