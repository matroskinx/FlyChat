package com.example.vladislav.flychat.AllChats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.vladislav.flychat.Models.LastMessage
import com.example.vladislav.flychat.Models.User


class AllChatsViewModel : ViewModel() {

    val remoteRepository = AllChatsRemoteRepository()
    val latestMessages: LiveData<MutableMap<String, LastMessage>> = Transformations.map(remoteRepository.latestMessages) {
        it
    }

    init {
        remoteRepository.loadChatsList()
    }
}