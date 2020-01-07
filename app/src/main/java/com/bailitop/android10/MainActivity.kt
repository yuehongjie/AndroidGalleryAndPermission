package com.bailitop.android10

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bailitop.gallery.bean.PhotoResult
import com.bailitop.gallery.constant.GalleryResult
import com.bailitop.gallery.ui.activity.GalleryActivity
import com.bailitop.gallery.ui.activity.TakePhotoActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    fun selectImage(view: View) {
        startActivity(Intent(this, SelectImageActivity::class.java))
    }

    fun takePhoto(view: View) {
        startActivityForResult(Intent(this, TakePhotoActivity::class.java), REQUEST_CODE_TAKE_PHOTO)
    }

    fun startSelfGallery(view: View) {
        startActivityForResult(Intent(this, GalleryActivity::class.java), REQUEST_CODE_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //从相册页面回传的数据
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == GalleryResult.RESULT_CODE_GALLERY) {

            val selectedList = data?.getParcelableArrayListExtra<PhotoResult>(GalleryResult.KEY_RESULT_SELECT_LIST)

            if (selectedList.isNullOrEmpty()) {
                Toast.makeText(this, "没有选择任何照片", Toast.LENGTH_LONG).show()
            }else {
                Toast.makeText(this, "选中 ${selectedList.size} 张照片", Toast.LENGTH_LONG).show()
            }
        }
        //拍照结果
        else if (requestCode == REQUEST_CODE_TAKE_PHOTO && resultCode == GalleryResult.RESULT_CODE_TAKE_PHOTO) {
            val photo: PhotoResult? = data?.getParcelableExtra(GalleryResult.KEY_RESULT_TAKE_PHOTO)
            if (photo == null) {
                Toast.makeText(this, "拍照失败", Toast.LENGTH_LONG).show()
            }else {
                Toast.makeText(this, "拍照成功：${photo.uri}", Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_GALLERY = 111
        private const val REQUEST_CODE_TAKE_PHOTO = 222
    }
}
