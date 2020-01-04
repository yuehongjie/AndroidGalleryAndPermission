package com.bailitop.android10

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bailitop.gallery.ui.activity.GalleryActivity

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

    fun openClassInTest(view: View) {
        val uri = Uri.parse("classin://www.eeo.cn/enterclass?telephone=13811837784&authTicket=xh9d0vjwww7bl6jp&classId=83066397&courseId=41968823&schoolId=4661092")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }else {
            Toast.makeText(this, "没有安装 ClassIn App", Toast.LENGTH_LONG).show()
        }
    }

    fun startSelfGallery(view: View) {
        startActivity(Intent(this, GalleryActivity::class.java))
    }
}
