package com.example.vladislav.flychat.Conversation


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.example.vladislav.flychat.ConversationFragmentArgs
import com.example.vladislav.flychat.R


class ConversationFragment : Fragment() {

    private val args: ConversationFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Toast.makeText(activity, args.chatId, Toast.LENGTH_LONG).show()
        return inflater.inflate(R.layout.fragment_conversation, container, false)
    }


}
