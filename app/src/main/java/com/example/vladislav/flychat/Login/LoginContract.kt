package com.example.vladislav.flychat.Login

import com.example.vladislav.flychat.BasePresenter
import com.example.vladislav.flychat.BaseView
import com.example.vladislav.flychat.LoadingView

interface LoginContract {
    interface Presenter : BasePresenter {
        fun onViewCreated()
        fun login(email: String, password: String)
        fun isUserLoggedIn(): Boolean
    }

    interface View : BaseView<Presenter>,
        LoadingView {
        fun setLoginError(exceptionMessage: String)
        fun navigateToRegister()
        fun navigateToChat()
    }
}