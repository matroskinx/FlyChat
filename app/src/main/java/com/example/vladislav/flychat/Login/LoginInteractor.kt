package com.example.vladislav.flychat.Login

import android.os.Handler

class LoginInteractor {
    interface OnLoginFinishListener {
        fun onLoginError()
        fun onSuccess()
    }

    fun login(email: String, password: String, listener: OnLoginFinishListener) {
        Handler().postDelayed({
            when {
                email.isEmpty() || password.isEmpty() -> listener.onLoginError()
                else -> listener.onSuccess()
            }
        }, 2000)

    }

}