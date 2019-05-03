package com.example.vladislav.flychat.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.vladislav.flychat.Models.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.HashMap

class AllChatsRemoteRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseDatabase = FirebaseDatabase.getInstance()

    val latestMessages = MutableLiveData<MutableMap<String, LastMessage>>()
    val userList = MutableLiveData<MutableMap<String, User>>()              // key = uid, value = user

    private val chatMembers = mutableMapOf<String, MutableList<String>>()   // key = chatId, value = listOf(user_id)
    val messageList = MutableLiveData<MutableList<ChatMessage>>()
    private lateinit var currentUser: User

    val uid: String = auth.uid!!

    val newChatId = MutableLiveData<String>()


    // CALLED FIRST
    fun loadChatsList() {

        val listener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val rawData = p0.value as HashMap<String, String>
                p0.key?.let {
                    val usr = convertRawDataToUser(it, rawData)
                    currentUser = usr
                    getLatestMessages(usr.chats)
                    getChatMembers(usr.chats)
                    getAvailableUsers()
                }
            }
        }

        db.getReference("users/$uid").addListenerForSingleValueEvent(listener)
    }

    private fun convertRawDataToUser(rawUid: String, rawData: HashMap<String, String>): User {
        //val rawUid = rawData["uid"] as String
        val rawImage = rawData["profileImageUrl"] ?: ""
        val rawEmail = rawData["email"] as String
        val rawUsername = rawData["name"] as String
        val rawChats: HashMap<String, String>? = rawData["chats"] as HashMap<String, String>?
        if (rawChats == null) {
            return User(rawUid, mutableListOf(), rawEmail, rawUsername, rawImage)
        } else {
            val chatIds = rawChats.keys.toMutableList()
            return User(rawUid, chatIds, rawEmail, rawUsername, rawImage)
        }
    }


    private fun getLatestMessages(chatIds: MutableList<String>) {
        val receivedLatestMessages = mutableMapOf<String, LastMessage>()
        chatIds.forEach { chatId ->
            db.getReference("chats/$chatId/lastMessage").addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val value = p0.getValue(LastMessage::class.java)
                    value?.let {
                        receivedLatestMessages[chatId] = p0.getValue(LastMessage::class.java) as LastMessage
                        latestMessages.postValue(receivedLatestMessages)
                    }
                }
            })
        }
    }

    fun getAvailableUsers() {
        /*
            get all users to start chat with someone
         */
        val receivedUserList = mutableMapOf<String, User>()
        db.getReference("users").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (item in p0.children) {
                    //p0.key
                    item.key?.let {
                        val usr = convertRawDataToUser(it, item.value as HashMap<String, String>)
                        receivedUserList[it] = usr
                        Log.d(TAG, "received ${usr.email}")
                    }

                }
                userList.postValue(receivedUserList)
            }
        })
    }

    fun sendMessage(text: String, toChatId: String, pictureUrl: String? = null) {
        val chat = ChatMessage(text, System.currentTimeMillis() / 1000, uid, pictureUrl)
        val lastMessage = LastMessage(text, chat.time)
        db.getReference("chats/$toChatId/messages").push().setValue(chat)
        db.getReference("chats/$toChatId/lastMessage").setValue(lastMessage)
    }

    fun sendPicture(text: String, toChatId: String, pictureUrl: String, width: Int, height: Int) {
        val chat = PictureMessage(System.currentTimeMillis() / 1000, uid, pictureUrl, width, height)
        val lastMessage = LastMessage(text, chat.time)
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
                        val rawData = it as HashMap<String, String>
                        val convertedData = rawData.keys.toMutableList()
                        chatMembers[chatId] = convertedData
                    }
                }
            })
        }
    }

    private fun loadChatMessages(chatId: String) {
        db.getReference("chats/$chatId/messages").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val receivedMessageList = mutableListOf<ChatMessage>()
                for (message in p0.children) {
                    receivedMessageList.add(message.getValue(ChatMessage::class.java) as ChatMessage)
                }
                messageList.postValue(receivedMessageList)
            }
        })
    }

    fun openChat(chatId: String) {
        loadChatMessages(chatId)
    }

    fun getUserNamesAndAvatars(fullUserList: MutableMap<String, User>): MutableMap<String, Pair<String, String>> {
        val list = mutableMapOf<String, Pair<String, String>>()  //key = chatId, value = pair(username, profileImageUrl)

        for ((chatId, uidList) in chatMembers) {
            val userId = uidList.single { u -> u != uid }
            val photoUrl = fullUserList[userId]?.profileImageUrl ?: ""
            val userName = fullUserList[userId]?.name ?: "Anonymous"
            list[chatId] = Pair(userName, photoUrl)
        }

        return list
    }


    fun startNewChat(destinationUid: String) {
        for ((chatId, list) in chatMembers) {
            if (list.contains(destinationUid)) {
                //openChat(chatId)
                newChatId.value = chatId
                return
            }
        }
        createNewChat(destinationUid)
    }

    private fun createNewChat(destinationUid: String) {
        val id = UUID.randomUUID().toString()
        val chat = Chat(id, LastMessage(), mutableListOf(), mutableListOf())

        val pairOne = hashMapOf("userId" to uid)
        val pairTwo = hashMapOf("userId" to destinationUid)


        db.getReference("chats/${chat.id}").setValue(chat).addOnCompleteListener {
            db.getReference("chats/${chat.id}/users/$uid").setValue(pairOne)
            db.getReference("chats/${chat.id}/users/$destinationUid").setValue(pairTwo)
        }
        addChatToUser(destinationUid, chat.id)


        newChatId.value = chat.id


        //TODO add listener onDataChange because in new chat messages wont load
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