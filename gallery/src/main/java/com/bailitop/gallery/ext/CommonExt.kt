package com.bailitop.gallery.ext

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import com.bailitop.gallery.scan.ScanEntity
import com.bailitop.gallery.scan.args.Columns

/**
 * 获取文件的 Uri
 */
fun ScanEntity.externalUri(): Uri {
    return if (mediaType == Columns.IMAGE) {
        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
    }else {
        ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
    }
}

fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}

//Android版本是否为M
fun hasM(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

//Android版本是否为N
fun hasN(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N

//Android版本是否为Q
fun hasQ(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

//判断颜色是否为亮色
fun Int.isLightColor(): Boolean = ColorUtils.calculateLuminance(this) >= 0.5

/** 设置状态栏颜色 */
fun Window.statusBarColor(@ColorInt color: Int) {
    if (hasM()) {
        statusBarColor = color
        if (color.isLightColor()) {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
    }
}