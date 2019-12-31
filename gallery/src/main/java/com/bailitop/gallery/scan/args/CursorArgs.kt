package com.bailitop.gallery.scan.args

import android.provider.MediaStore
import com.bailitop.gallery.scan.ScanEntity

internal object CursorArgs {

    /**
     * 扫描的 Uri：外部存储
     */
    val FILE_URI = MediaStore.Files.getContentUri("external")

    /**
     * 查询的字段信息，对应图片信息 [ScanEntity]
     */
    val ALL_COLUMNS = arrayOf(
        Columns.ID,
        Columns.SIZE,
        Columns.DURATION,
        Columns.PARENT,
        Columns.MIME_TYPE,
        Columns.DISPLAY_NAME,
        Columns.ORIENTATION,
        Columns.BUCKET_ID,
        Columns.BUCKET_DISPLAY_NAME,
        Columns.MEDIA_TYPE,
        Columns.WIDTH,
        Columns.HEIGHT,
        Columns.DATE_MODIFIED
    )
}