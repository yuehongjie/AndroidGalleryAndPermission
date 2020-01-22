package com.bailitop.android10.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.fragment.app.Fragment


val hasAndroid10 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

val hasAndroid7 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N

val hasAndroid6 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M


/**
 *  7.0 以上需要，调用系统 Intent 需要授予临时读 or 写权限
 *
 *  writeAble 是否需要 FLAG_GRANT_WRITE_URI_PERMISSION 权限 ，如拍照不需要，下载安装 apk 需要
 */
fun Intent.addReadWritePermission(readable: Boolean = true, writable: Boolean = false) {

    // 7.0 以上需要授予临时读写权限
    if (readable) {
        this.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    if (writable) {
        this.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    }
}

fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}

fun Fragment.toast(msg: String) {
    context?.toast(msg)
}