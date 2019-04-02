package com.example.vladislav.flychat.Register

import android.net.Uri

class RegisterPresenter(private var view: RegisterContract.View?, private val registerInteractor: RegisterInteractor) :
    RegisterContract.Presenter, RegisterInteractor.OnRegisterFinishListener {
    override fun onRegisterError(exceptionMessage: String) {
        view?.setRegisterError(exceptionMessage)
        view?.hideLoading()
    }

    override fun onSuccess() {
        view?.navigateToChat()
    }

    override fun onViewCreated() {

    }

    override fun register(username: String, email: String, password: String, photoFileUri: Uri?) {
        view?.showLoading()
        registerInteractor.register(username, email, password, photoFileUri, this)
    }

    override fun onDestroy() {
        this.view = null
    }
}