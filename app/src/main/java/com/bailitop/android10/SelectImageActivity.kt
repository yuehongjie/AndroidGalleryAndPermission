package com.bailitop.android10

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_select_image.*

class SelectImageActivity : AppCompatActivity() {

    companion object {
        //调用相册获取图片
        const val SELECT_IMAGE_REQUEST_CODE = 0x03
        //获取权限
        const val PERMISSION_STORAGE_REQUEST_CODE = 0x04
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_image)
    }

    fun selectImage(view: View) {
        checkPermissionAndAlarm()
    }

    private fun checkPermissionAndAlarm(){

        openAlarm()
        /*
        //检查权限
        val hasPermission = ContextCompat.checkSelfPermission(application, Manifest.permission.READ_EXTERNAL_STORAGE)
        if (hasPermission == PackageManager.PERMISSION_GRANTED) {
            //有权限，调用相机拍照
            openAlarm()
        }else {
            //没有权限，申请权限
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_STORAGE_REQUEST_CODE)
        }*/
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_STORAGE_REQUEST_CODE) {
            var isAllGranted = true
            for (grant in grantResults) {
                if (grant == PackageManager.PERMISSION_DENIED) {
                    isAllGranted = false
                    break
                }
            }

            if (isAllGranted) {
                // 同意所有需要的权限，调用相机拍照
                openAlarm()
            }else {
                toast("没有权限")
            }
        }
    }

    private fun openAlarm(){
        val alarmIntent = Intent(Intent.ACTION_PICK)
        alarmIntent.type = "image/*"
        startActivityForResult(alarmIntent, SELECT_IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val imageUri = data?.data
                log("选择图片：$imageUri")
                if (imageUri != null) {
                    ivShow.setImageURI(imageUri)
                }
            }
        }
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun log(msg: String) {
        Log.e("Android10Test", msg)
    }
}
