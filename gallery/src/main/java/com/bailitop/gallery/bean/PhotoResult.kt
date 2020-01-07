package com.bailitop.gallery.bean

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 照片选择结果 或者拍照结果 或者裁剪结果，为了兼容各个版本，只返回 uri，不返回绝对路径了
 */
@Parcelize
data class PhotoResult(
    val uri: Uri?
): Parcelable