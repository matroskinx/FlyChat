package com.example.vladislav.flychat.AllChats

import android.util.Log
import com.example.vladislav.flychat.Models.Chat
import com.example.vladislav.flychat.Models.ChatMessage
import com.example.vladislav.flychat.Models.LastMessage
import com.example.vladislav.flychat.Models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.HashMap

class AllChatsRemoteRepository {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var db: FirebaseDatabase = FirebaseDatabase.getInstance()

    val latestMessages = mutableMapOf<String, LastMessage>()
    private val chatMembers = mutableMapOf<String, ArrayList<String>>()        // key = chatId, value = listOf(user_id)
    val userList = mutableMapOf<String, User>()
    private lateinit var currentUser: User

    private val uid: String = auth.uid!!

    // CALLED FIRST
    fun loadChatsList() {

        val listener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val rawData = p0.value as HashMap<String, String>
                val usr = convertRawDataToUser(rawData)
                currentUser = usr
                getLatestMessages(usr.chats)
                getChatMembers(usr.chats)
            }
        }

        db.getReference("users/$uid").addListenerForSingleValueEvent(listener)
    }

    private fun convertRawDataToUser(rawData: HashMap<String, String>): User {
        val rawUid = rawData["uid"] as String
        val rawImage = rawData["profileImageURL"] as String
        val rawEmail = rawData["email"] as String
        val rawUsername = rawData["username"] as String
        val rawChats: HashMap<String, String>? = rawData["chats"] as HashMap<String, String>?
        if (rawChats == null) {
            return User(rawUid, mutableListOf(), rawEmail, rawUsername, rawImage)
        } else {
            val chatIds = rawChats.keys.toMutableList()
            return User(rawUid, chatIds, rawEmail, rawUsername, rawImage)
        }
    }


    private fun getLatestMessages(chatIds: MutableList<String>) {
        chatIds.forEach { chatId ->
            db.getReference("chats/$chatId/lastMessage").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    //TODO null check if chat is empty
                    val value = p0.getValue(LastMessage::class.java)
                    value?.let {
                        latestMessages[chatId] = p0.getValue(LastMessage::class.java) as LastMessage
                    }
                }
            })
        }
    }

    fun getAvailableUsers() {
        /*
            get all users to start chat with someone
         */
        db.getReference("users").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (item in p0.children) {
                    val usr = convertRawDataToUser(item.value as HashMap<String, String>)
                    userList[usr.uid] = usr
                    Log.d(TAG, "received ${usr.email}")
                }
            }
        })
    }

    fun sendMessage(text: String, toChatId: String) {
        val chat = ChatMessage(text, System.currentTimeMillis(), "TEMP_UNAME")
        val lastMessage = LastMessage(text, chat.timestamp)
        db.getReference("chats/$toChatId/messages").push().setValue(chat)
        db.getReference("chats/$toChatId/lastMessage").setValue(lastMessage)
    }

    private fun getChatMembers(chatIds: MutableList<String>) {
        /*
            get all members with whom user has chats with
        */

        for (chatId in chatIds) {
            db.getReference("chats/$chatId/users").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {

                    p0.value?.let {
                        chatMembers[chatId] = it as ArrayList<String>
                    }
                }
            })
        }
    }

    private fun loadChatMessages(chatId: String) {
        db.getReference("chats/$chatId/messages").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val messageList = mutableListOf<ChatMessage>()
                for (message in p0.children) {
                    messageList.add(message.getValue(ChatMessage::class.java) as ChatMessage)
                }
            }
        })
    }

    fun openChat(chatId: String) {
        loadChatMessages(chatId)
    }

    fun startNewChat(destinationUid: String) {
        for ((chatId, list) in chatMembers) {
            if (list.contains(destinationUid)) {
                openChat(chatId)
                return
            }
        }
        createNewChat(destinationUid)
    }

    private fun createNewChat(destinationUid: String) {
        val id = UUID.randomUUID().toString()
        val chat = Chat(id, LastMessage(), mutableListOf(), mutableListOf(uid, destinationUid))
        db.getReference("chats/${chat.id}").setValue(chat)
        addChatToUser(destinationUid, chat.id)
    }

    private fun addChatToUser(destinationUid: String, chatId: String) {
        val value = hashMapOf("chatId" to chatId)
        db.getReference("users/$destinationUid/chats/$chatId").setValue(value)
        db.getReference("users/$uid/chats/$chatId").setValue(value)
    }

    companion object {
        const val TAG = "AllChatsRemoteRepo"
    }
}