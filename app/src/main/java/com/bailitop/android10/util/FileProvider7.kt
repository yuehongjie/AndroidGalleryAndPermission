package com.bailitop.android10.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

/**
 * Android7.0 - Android9.0
 * 文件不能直接使用 file://   需要使用 FileProvider 创建一个content 类型的 Uri，即 content://
 *
 * 10.0 文件分区存储，uri 也不行了，需要另外适配
 */
object FileProvider7 {

    fun getUriForFile(context: Context, file: File): Uri {

        return if (hasAndroid7) {
            FileProvider.getUriForFile(context, context.packageName, file)
        }else {
            Uri.fromFile(file)
        }
    }



}