package com.example.vladislav.flychat.Login

import android.os.Handler
import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class LoginInteractor {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val TAG = "LoginInteractor"

    interface OnLoginFinishListener {
        fun onLoginError(exceptionMessage: String)
        fun onSuccess()
    }

    fun login(email: String, password: String, listener: OnLoginFinishListener) {
        if (email.isEmpty() or password.isEmpty()) {
            listener.onLoginError("Fields should not be empty")
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "successful sign in")
                    listener.onSuccess()
                } else {
                    Log.d(TAG, "unsuccessful log in")
                    listener.onLoginError(task.exception?.message as String)
                }
            }
    }


    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

}