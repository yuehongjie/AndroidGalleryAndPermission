package com.bailitop.android10

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bailitop.android10.util.FileProvider7
import com.bailitop.android10.util.addReadWritePermission
import com.bailitop.android10.util.hasAndroid10
import kotlinx.android.synthetic.main.activity_take_photo.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class TakePhotoActivity : AppCompatActivity() {

    companion object {
        // 申请相机权限的 requestCode
        const val PERMISSION_CAMERA_REQUEST_CODE = 0x01

        // 拍照的 requestCode
        const val CAMERA_REQUEST_CODE = 0x02
    }

    private var mPhotoUri: Uri ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_photo)
    }

    fun takePhoto(view: View) {

        checkPermissionAndCamera()

    }

    /**
     * 检查权限并拍照
     */
    private fun checkPermissionAndCamera() {

        //检查相机权限
        val hasPermission = ContextCompat.checkSelfPermission(application, Manifest.permission.CAMERA)
        if (hasPermission == PackageManager.PERMISSION_GRANTED) {
            //有权限，调用相机拍照
            openCamera()
        }else {
            //没有权限，申请权限
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), PERMISSION_CAMERA_REQUEST_CODE)
        }
    }

    /**
     * 处理权限申请回调
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        //相机权限申请
        if (requestCode == PERMISSION_CAMERA_REQUEST_CODE) {

            var isAllGranted = true
            for (grant in grantResults) {
                if (grant == PackageManager.PERMISSION_DENIED) {
                    isAllGranted = false
                    break
                }
            }

            if (isAllGranted) {
                // 同意所有需要的权限，调用相机拍照
                openCamera()
            }else {
                toast("没有权限")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                ivShow.setImageURI(mPhotoUri)
            }
        }
    }


    private fun openCamera() {

        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //判断是否有相机
        if (captureIntent.resolveActivity(packageManager) != null) {

            mPhotoUri = if (hasAndroid10) {
                createImageUriAfterQ()
            }else {
                createImageUriBeforeQ()
            }

            log("photoUri: $mPhotoUri")

            if (mPhotoUri != null) {
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri)
                captureIntent.addReadWritePermission(readable = true, writable = true)
                startActivityForResult(captureIntent, CAMERA_REQUEST_CODE)
            }

        }

    }


    private fun createImageUriAfterQ(): Uri? {
        log("after android10")
        val status = Environment.getExternalStorageState()
        return if (Environment.MEDIA_MOUNTED == status ) {
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues())
        }else {
            contentResolver.insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, ContentValues())
        }
    }

    /**
     * Android10 之前，创建本地文件
     */
    private fun createImageUriBeforeQ(): Uri {
        log("before android10")
        val currDate = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
        val imageName = "${currDate}.jpg"
        //使用私有的外部存储，不需要存储权限，如果使用公有的，则需要存储权限
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (!storageDir!!.exists()) {
            storageDir.mkdirs()
        }
        val tempFile = File(storageDir, imageName)
        return FileProvider7.getUriForFile(this, tempFile)
    }


    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun log(msg: String) {
        Log.e("Android10Test", msg)
    }
}
