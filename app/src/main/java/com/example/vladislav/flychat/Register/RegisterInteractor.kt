package com.example.vladislav.flychat.Register

import android.os.Handler

class RegisterInteractor {
    interface OnRegisterFinishListener {
        fun onRegisterError()
        fun onSuccess()
    }

    fun register(username: String, email: String, password: String, listener: OnRegisterFinishListener) {
        Handler().postDelayed({
            when {
                username.isEmpty() || email.isEmpty() || password.isEmpty() -> listener.onRegisterError()
                else -> listener.onSuccess()
            }
        }, 2000)
    }
}