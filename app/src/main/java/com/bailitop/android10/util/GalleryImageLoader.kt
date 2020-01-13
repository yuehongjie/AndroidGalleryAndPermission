package com.bailitop.android10.util

import android.net.Uri
import android.widget.ImageView
import com.bailitop.android10.R
import com.bailitop.gallery.loader.IGalleryImageLoader
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.parcel.Parcelize

@Parcelize
class GalleryImageLoader : IGalleryImageLoader{
    override fun displayGalleryImage(view: ImageView, uri: Uri, size: Int) {
        val options: RequestOptions = RequestOptions()
            .placeholder(R.drawable.ic_gallery_default_loading)
            .override(size)
        Glide.with(view).load(uri).apply(options).into(view)
    }

    override fun displayFinderImage(view: ImageView, uri: Uri) {
        displayImage(view, uri)
    }

    override fun displayPreviewImage(view: ImageView, uri: Uri) {
        displayImage(view, uri)
    }

    private fun displayImage(view: ImageView, uri: Uri) {
        val options: RequestOptions = RequestOptions()
            .placeholder(R.drawable.ic_gallery_default_loading)
        Glide.with(view).load(uri).apply(options).into(view)
    }

}