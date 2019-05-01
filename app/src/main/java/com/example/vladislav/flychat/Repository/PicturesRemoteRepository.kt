package com.example.vladislav.flychat.Repository

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.util.*
import android.graphics.BitmapFactory



class PicturesRemoteRepository {
    private val storage = FirebaseStorage.getInstance()

    interface OnDownloadResult {
        fun test(file: File)
    }

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

    fun downloadPicture(url: String, listener: OnDownloadResult) {
        val ref = storage.getReferenceFromUrl(url)

        val file: File = createTempFile()

        ref.getFile(file).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                listener.test(file)
                Log.d("PicturesRepo", "successful file load")
            } else {
                Log.d("PicturesRepo", "unsuccessful file load")
            }
        }
    }
}