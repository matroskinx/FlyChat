package com.example.vladislav.flychat.Register

import android.net.Uri
import android.util.Log
import com.example.vladislav.flychat.Models.Chat
import com.example.vladislav.flychat.Models.LastMessage
import com.example.vladislav.flychat.Models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class RegisterInteractor {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var db: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val storage = FirebaseStorage.getInstance().reference

    interface OnRegisterFinishListener {
        fun onRegisterError(exceptionMessage: String)
        fun onSuccess()
    }

    fun register(
        username: String,
        email: String,
        password: String,
        photoFileUri: Uri?,
        listener: OnRegisterFinishListener
    ) {
        //TODO username for registration
        if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
            listener.onRegisterError("Fields should not be empty")
            return
        }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.continueWith {
                        saveUserToFirebase(username, email, photoFileUri, listener)
                    }
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "register successful")
                } else {
                    // If sign in fails, display a message to the user.
                    Log.d(TAG, "register unsuccessful")
                    listener.onRegisterError(task.exception?.message ?: "")
                }
            }
    }

    private fun saveUserToFirebase(
        username: String,
        email: String,
        photoFileUri: Uri?,
        listener: OnRegisterFinishListener
    ) {
        val uid = auth.uid
        val reference = db.getReference("users/$uid")
        //val chatsref = db.getReference("chats/smth")
        uid?.let {
            val user = User(uid, email = email, username = username, profileImageURL = photoFileUri.toString())
            reference.setValue(user)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        listener.onSuccess()
                        Log.d(TAG, "User with uid: $uid saved successfully")
                    } else {
                        listener.onRegisterError(task.exception?.localizedMessage ?: "")
                        Log.d(TAG, "Failed to save user $uid because of: ${task.exception?.localizedMessage}")
                    }
                }.continueWith {
                    /*
                    val chat = Chat("smth", LastMessage("123", 34214, "Vladislav"), mutableListOf(), mutableListOf())
                    chatsref.setValue(chat)
                    */
                }
            return
        }
    }


/*    private fun uploadPicToFirebase(photoFileUri: Uri) {
        val filename = UUID.randomUUID().toString()
        val fileRef = storage.child("avatars/$filename")

        val uploadTask = fileRef.putFile(photoFileUri)

        uploadTask.addOnSuccessListener {
            Log.d(TAG, "picture successfully uploaded")
            fileRef.downloadUrl.addOnSuccessListener { uri ->
                Log.d(TAG, "upload link: $uri")
            }
        }
        uploadTask.addOnFailureListener { exception ->
            Log.d(TAG, "picture not loaded, reason: ${exception.localizedMessage}")
        }
    }*/

    companion object {
        const val TAG = "RegisterInteractor"
    }
}