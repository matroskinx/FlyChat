package com.example.vladislav.flychat.Register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.vladislav.flychat.AllChats.AllChatsActivity
import com.example.vladislav.flychat.Login.LoginActivity
import com.example.vladislav.flychat.R
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(), RegisterContract.View {

    lateinit var presenter: RegisterContract.Presenter

    override fun setRegisterError(exceptionMessage: String) {
        username_field.error = "This field is necessary"
        email_field.error = "This field is necessary"
        password_field.error = "This field is necessary"
        Toast.makeText(this, exceptionMessage, Toast.LENGTH_LONG).show()
    }

    override fun navigateToLogin() {
        finish()
    }

    override fun navigateToChat() {
        startActivity(
            Intent(
                this,
                AllChatsActivity::class.java
            ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
    }

    override fun setupPresenter(presenter: RegisterContract.Presenter) {
        this.presenter = presenter
    }

    override fun showLoading() {
        register_progress_bar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        register_progress_bar.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setupPresenter(
            RegisterPresenter(
                this,
                RegisterInteractor()
            )
        )

        presenter.onViewCreated()

        to_login_btn.setOnClickListener {
            finish()
        }

        perform_register.setOnClickListener {
            presenter.register(
                username_field.text.toString(),
                email_field.text.toString(),
                password_field.text.toString()
            )
        }

        profile_image.setOnClickListener {
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Choose from?")
                .setItems(arrayOf("Gallery", "Camera")) { _, which ->
                    when (which) {
                        0 -> Log.d("registerActivity", "Selected gallery")
                        //TODO pick from gallery and take picture
                        1 -> Log.d("registerActivity", "Selected camera")
                    }
                }
                .show()
        }
    }
}
