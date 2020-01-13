package com.bailitop.gallery.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class GalleryConfig(
    /** 最大选择数 */
    val maxSelectCount: Int = 3,
    /** 图片按一行几列展示 */
    val spanCount: Int = 4,
    /** 相册标题 */
    val galleryTitle: String = "选择图片",
    /** 全部目录显示的名字 */
    val allFinderName: String = "全部"
) : Parcelable