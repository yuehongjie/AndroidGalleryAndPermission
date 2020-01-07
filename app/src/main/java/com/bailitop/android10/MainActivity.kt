package com.bailitop.android10

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bailitop.gallery.constant.GalleryConstant
import com.bailitop.gallery.scan.ScanEntity
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
        startActivityForResult(Intent(this, GalleryActivity::class.java), GalleryConstant.REQUEST_CODE_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //从相册页面回传的数据
        if (requestCode == GalleryConstant.REQUEST_CODE_GALLERY &&
            resultCode == GalleryConstant.RESULT_CODE_GALLERY) {

            val resultBundle = data?.extras ?: Bundle.EMPTY

            val selectedList = resultBundle.getParcelableArrayList<ScanEntity>(GalleryConstant.KEY_SELECT_LIST)

            if (selectedList.isNullOrEmpty()) {
                Toast.makeText(this, "没有选择任何照片", Toast.LENGTH_LONG).show()
            }else {
                Toast.makeText(this, "选中 ${selectedList.size} 张照片", Toast.LENGTH_LONG).show()
            }
        }
    }
}
