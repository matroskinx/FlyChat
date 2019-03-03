package com.example.vladislav.flychat.Register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.vladislav.flychat.Login.LoginActivity
import com.example.vladislav.flychat.R
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(), RegisterContract.View {

    lateinit var presenter: RegisterContract.Presenter

    override fun setRegisterError() {
        username_field.error = "This field is necessary"
        email_field.error = "This field is necessary"
        password_field.error = "This field is necessary"
    }

    override fun navigateToLogin() {
        finish()
    }

    override fun navigateToChat() {
        Toast.makeText(this, "Registered successfully", Toast.LENGTH_LONG).show()
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

        perform_register.setOnClickListener{
            presenter.register(username_field.toString(), email_field.toString(), password_field.toString())
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
