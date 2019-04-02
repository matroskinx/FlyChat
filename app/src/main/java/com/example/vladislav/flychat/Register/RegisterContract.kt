package com.example.vladislav.flychat.Register

import android.net.Uri
import com.example.vladislav.flychat.BasePresenter
import com.example.vladislav.flychat.BaseView
import com.example.vladislav.flychat.LoadingView

interface RegisterContract {
    interface Presenter : BasePresenter {
        fun onViewCreated()
        fun register(username: String, email: String, password: String, photoFileUri: Uri?)
    }

    interface View : BaseView<Presenter>,
        LoadingView {
        fun setRegisterError(exceptionMessage: String)
        fun navigateToLogin()
        fun navigateToChat()
    }
}