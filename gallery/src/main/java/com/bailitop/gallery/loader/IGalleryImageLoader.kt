package com.bailitop.gallery.loader

import android.net.Uri
import android.os.Parcelable
import android.widget.ImageView

interface IGalleryImageLoader: Parcelable {

    /**
     * 显示相册列表图片
     */
    fun displayGalleryImage(view: ImageView, uri: Uri, size: Int)

    /**
     * 显示目录图片
     */
    fun displayFinderImage(view: ImageView, uri: Uri)


    /**
     * 显示预览页图片
     */
    fun displayPreviewImage(view: ImageView, uri: Uri)

}