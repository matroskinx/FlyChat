package com.example.vladislav.flychat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_picture.*

class PictureActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture)
        photo_view.setImageResource(R.drawable.ic_launcher_background)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val url = intent.getStringExtra(URL_KEY)
        Picasso.get()
            .load(url)
            .into(photo_view)
    }

    companion object {
        const val URL_KEY = "pic_url"
    }
}
