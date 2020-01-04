package com.bailitop.gallery.ext

import android.net.Uri
import android.widget.ImageView
import com.bailitop.gallery.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.display(uri: Uri, size: Int){

    val options: RequestOptions = RequestOptions()
        .placeholder(R.drawable.ic_gallery_default_loading)
        .override(size)
    Glide.with(this).load(uri).apply(options).into(this)

}