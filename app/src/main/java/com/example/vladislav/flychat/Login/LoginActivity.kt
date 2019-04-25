package com.example.vladislav.flychat.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.vladislav.flychat.AllChats.AllChatsActivity
import com.example.vladislav.flychat.R
import com.example.vladislav.flychat.Register.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginContract.View {

    lateinit var presenter: LoginContract.Presenter

    override fun navigateToChat() {
        startActivity(Intent(this, AllChatsActivity::class.java))
        finish()
    }

    override fun setLoginError(exceptionMessage: String) {
        email_field_login.error = "This field is necessary"
        password_field_login.error = "This field is necessary"
        Toast.makeText(this, exceptionMessage, Toast.LENGTH_LONG).show()
    }

    override fun navigateToRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    override fun setupPresenter(presenter: LoginContract.Presenter) {
        this.presenter = presenter
    }

    override fun showLoading() {
        login_progress_bar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        login_progress_bar.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupPresenter(
            LoginPresenter(
                this,
                LoginInteractor()
            )
        )
        presenter.onViewCreated()

        to_register_btn.setOnClickListener {
            navigateToRegister()
            it.isEnabled = false
        }

        perform_login.setOnClickListener {
            presenter.login(email_field_login.text.toString(), password_field_login.text.toString())
        }
    }

    override fun onStart() {
        super.onStart()
        if (presenter.isUserLoggedIn()) {
            startActivity(Intent(this, AllChatsActivity::class.java))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        to_register_btn.isEnabled = true
    }
}
