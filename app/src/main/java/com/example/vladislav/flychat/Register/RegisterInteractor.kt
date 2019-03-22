package com.example.vladislav.flychat.Register

import android.os.Handler
import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class RegisterInteractor {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val TAG = "RegisterInteractor"

    interface OnRegisterFinishListener {
        fun onRegisterError(exceptionMessage: String)
        fun onSuccess()
    }

    fun register(username: String, email: String, password: String, listener: OnRegisterFinishListener) {
        //TODO username for registration
        if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
            listener.onRegisterError("Fields should not be empty")
            return
        }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "register successful")
                    listener.onSuccess()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.d(TAG, "register unsuccessful")
                    listener.onRegisterError(task.exception?.message as String)
                }
            }
    }
}