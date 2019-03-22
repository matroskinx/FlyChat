package com.example.vladislav.flychat.Register

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

    override fun register(username: String, email: String, password: String) {
        view?.showLoading()
        registerInteractor.register(username, email, password, this)
    }

    override fun onDestroy() {
        this.view = null
    }
}