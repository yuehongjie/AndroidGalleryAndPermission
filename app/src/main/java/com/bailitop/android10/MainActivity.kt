package com.bailitop.android10

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun takePhoto(view: View) {
        startActivity(Intent(this, TakePhotoActivity::class.java))
    }

    fun selectImage(view: View) {
        startActivity(Intent(this, SelectImageActivity::class.java))
    }
}
