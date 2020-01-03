package com.bailitop.gallery.scan

import android.os.Parcelable
import com.bailitop.gallery.scan.args.Columns
import kotlinx.android.parcel.Parcelize

// @Parcelize 是 kotlin 扩展，自动帮忙生成可序列化的代码
@Parcelize
data class ScanEntity(
    var id: Long = 0,
    var size: Long = 0,
    var duration: Long = 0,
    var parent: Long = 0,
    var mimeType: String = "",
    var displayName: String = "",
    var orientation: Int = 0,
    var bucketId: String = "",
    var bucketDisplayName: String = "",
    var mediaType: String = Columns.IMAGE,
    var width: Int = 0,
    var height: Int = 0,
    var dataModified: Long = 0,

    /** 当前目录下的照片数 */
    var count: Int = 0,

    /** 该图片是否被选中 */
    var isCheck: Boolean = false) : Parcelable