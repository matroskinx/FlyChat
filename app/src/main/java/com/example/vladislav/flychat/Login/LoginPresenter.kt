package com.example.vladislav.flychat.Login

class LoginPresenter(private var view: LoginContract.View?, private val loginInteractor: LoginInteractor) :
    LoginContract.Presenter,
    LoginInteractor.OnLoginFinishListener {

    override fun onLoginError() {
        view?.setLoginError()
        view?.hideLoading()
    }

    override fun onSuccess() {
        view?.navigateToChat()
    }

    override fun login(email: String, password: String) {
        view?.showLoading()
        loginInteractor.login(email, password, this)
    }

    override fun onViewCreated() {
    }

    override fun onDestroy() {
        this.view = null
    }
}