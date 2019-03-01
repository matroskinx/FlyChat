package com.example.vladislav.flychat

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        to_register_btn.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
            it.isEnabled = false
        }
    }

    override fun onResume() {
        super.onResume()
        to_register_btn.isEnabled = true
    }
}
