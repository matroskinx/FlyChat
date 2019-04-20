package com.example.vladislav.flychat.AllChats

import com.example.vladislav.flychat.Models.LastMessage
import com.example.vladislav.flychat.Models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class AllChatsRemoteRepository {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var db: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val storage = FirebaseStorage.getInstance().reference

    private val latestChats = mutableMapOf<String, LastMessage>()

    private val uid: String = auth.uid!!

    fun loadChatsList() {

        val listener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val usr = p0.getValue(User::class.java)
                getLatestMessages(usr?.chats as MutableList<String>)
            }
        }

        db.getReference("users/$uid").addValueEventListener(listener)

    }

    private fun getLatestMessages(chatIds: MutableList<String>) {
        chatIds.forEach { id ->
            db.getReference("chats/$id/lastMessage").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }
                override fun onDataChange(p0: DataSnapshot) {
                    latestChats[id] = p0.getValue(LastMessage::class.java) as LastMessage
                }
            })
        }
    }
}