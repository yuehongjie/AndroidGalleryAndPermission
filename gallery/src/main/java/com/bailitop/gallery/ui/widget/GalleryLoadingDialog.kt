package com.bailitop.gallery.ui.widget

import android.app.Dialog
import android.content.Context
import com.bailitop.gallery.R

class GalleryLoadingDialog(context: Context) : Dialog(context, R.style.GalleryLoadingDialogStyle) {

    init {
        setContentView(R.layout.dialog_gallery_loading)
    }

}