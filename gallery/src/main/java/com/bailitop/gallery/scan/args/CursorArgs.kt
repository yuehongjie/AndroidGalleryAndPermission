package com.bailitop.gallery.scan.args

import android.provider.MediaStore
import com.bailitop.gallery.scan.ScanConst
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

    /**
     * 排序条件：修改时间倒叙
     */
    const val ORDER_BY = "${ Columns.DATE_MODIFIED} DESC"

    /**
     * 基础查询条件：目前是两个 MEDIA_TYPE ，实际查询时，可能只有一个填值，如果
     */
    const val ALL_SELECTION = "${Columns.SIZE} > 0 and ${Columns.MEDIA_TYPE} =? or ${Columns.MEDIA_TYPE} =? "

    /**
     * 查询条件：查找某个目录下的
     */
    fun getParentSelection(parent: Long) = "${Columns.PARENT} = $parent and ($ALL_SELECTION)"

    /**
     * 查询条件：查询某个媒体文件
     */
    fun getSingleSelection(id: Long) = """${Columns.ID} = "$id" and ($ALL_SELECTION)"""

    /**
     * 查询类型，这里是条件中 MEDIA_TYPE =? 对应的值
     */
    fun getSelectioneArgs(scanType: Int): Array<String> = when(scanType) {
        ScanConst.VIDEO -> arrayOf(Columns.VIDEO)
        ScanConst.IMAGE -> arrayOf(Columns.IMAGE)
        else -> arrayOf(Columns.IMAGE, Columns.VIDEO) // ScanConst.MIX
    }
}