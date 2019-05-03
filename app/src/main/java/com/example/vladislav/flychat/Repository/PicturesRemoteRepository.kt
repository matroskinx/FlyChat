package com.example.vladislav.flychat.Repository

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.util.*
import android.graphics.BitmapFactory


class PicturesRemoteRepository {
    private val storage = FirebaseStorage.getInstance()

    interface OnUploadResult {
        fun onUploadSuccess(downloadLink: String, width: Int, height: Int)
    }

    fun uploadPicture(photoUri: String, width: Int, height: Int, chatId: String, listener: OnUploadResult) {
        val filename = UUID.randomUUID().toString()
        val ref = storage.getReference("messageImages/$chatId/$filename")

        val uri = Uri.parse(photoUri)

        val uploadTask = ref.putFile(uri)

        uploadTask.addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener {
                Log.d("PicturesRepo", "upload picture link:$it")
                listener.onUploadSuccess(it.toString(), width, height)
            }
        }

        uploadTask.addOnFailureListener { exception ->
            Log.d("PicturesRepo", "picture not loaded, reason: ${exception.localizedMessage}")
        }
    }
}