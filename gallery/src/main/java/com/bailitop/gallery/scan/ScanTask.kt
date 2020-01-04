package com.bailitop.gallery.scan

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.bailitop.gallery.scan.args.Columns
import com.bailitop.gallery.scan.args.CursorArgs
import java.util.*

/**
 * 扫描图片要执行的任务
 * loaderSuccess：扫描成功后，调用的方法，是一个高阶函数
 */
class ScanTask(private val context: Context, private val loaderSuccess: (LinkedList<ScanEntity>) -> Unit):LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * 扫描结果
     */
    private val resultList = LinkedList<ScanEntity>()

    // 构建查询条件 创建查询器
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        Log.d("ScanTask", "onCreateLoader: $id")
        //传入的参数

        //扫描文件夹
        val scanParent = args?.getLong(Columns.PARENT) ?: ScanConst.ALL
        //扫描某个文件，如拍照后，扫描那一张照片
        val scanSingleFile = args?.getLong(Columns.ID) ?: 0L
        //扫描的类型，默认图片
        val scanType = args?.getInt(Columns.SCAN_TYPE) ?: ScanConst.IMAGE

        //拼接扫描条件
        val selection = when {
            //扫描单个文件
            scanSingleFile != 0L -> CursorArgs.getSingleSelection(scanSingleFile)
            //扫描全部
            scanParent == ScanConst.ALL -> CursorArgs.ALL_SELECTION
            //扫描某个文件夹
            else -> CursorArgs.getParentSelection(scanParent)
        }

        Log.d("ScanTask", "scanParent: $scanParent ; scanSingleFile: $scanSingleFile ; scanType: $scanType ; selection: $selection")

        return CursorLoader(
            context,
            CursorArgs.FILE_URI,    //查询外部媒体
            CursorArgs.ALL_COLUMNS, //查询的字段
            selection,              //查询条件
            CursorArgs.getSelectioneArgs(scanType), //查询条件 ? 对应的值，这里是类型
            CursorArgs.ORDER_BY     //排序条件
        )
    }

    //加载完成
    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {

        Log.d("ScanTask", "onLoadFinished")

        val cursor = data?: return // 查询失败

        // 取数据，先确定索引
        val idColumnIndex = cursor.getColumnIndex(Columns.ID)
        val sizeColumnIndex = cursor.getColumnIndex(Columns.SIZE)
        val durationColumnIndex = cursor.getColumnIndex(Columns.DURATION)
        val parentColumnIndex = cursor.getColumnIndex(Columns.PARENT)
        val mimeTypeColumnIndex = cursor.getColumnIndex(Columns.MIME_TYPE)
        val displayNameColumnIndex = cursor.getColumnIndex(Columns.DISPLAY_NAME)
        val orientationColumnIndex = cursor.getColumnIndex(Columns.ORIENTATION)
        val bucketIdColumnIndex = cursor.getColumnIndex(Columns.BUCKET_ID)
        val bucketDisplayNameColumnIndex = cursor.getColumnIndex(Columns.BUCKET_DISPLAY_NAME)
        val mediaTypeColumnIndex = cursor.getColumnIndex(Columns.MEDIA_TYPE)
        val widthColumnIndex = cursor.getColumnIndex(Columns.WIDTH)
        val heightColumnIndex = cursor.getColumnIndex(Columns.HEIGHT)
        val dateModifiedColumnIndex = cursor.getColumnIndex(Columns.DATE_MODIFIED)

        //取数据 循环
        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumnIndex)
            val size = cursor.getLong(sizeColumnIndex)
            val duration = cursor.getLong(durationColumnIndex)
            val parent = cursor.getLong(parentColumnIndex)
            val mimeType = cursor.getString(mimeTypeColumnIndex)
            val displayName = cursor.getString(displayNameColumnIndex)
            val orientation = cursor.getInt(orientationColumnIndex)
            val bucketId = cursor.getString(bucketIdColumnIndex)
            val bucketDisplayName = cursor.getString(bucketDisplayNameColumnIndex)
            val mediaType = cursor.getString(mediaTypeColumnIndex)
            val width = cursor.getInt(widthColumnIndex)
            val height = cursor.getInt(heightColumnIndex)
            val dataModified = cursor.getLong(dateModifiedColumnIndex)

            resultList.add(
                ScanEntity(id, size, duration, parent, mimeType, displayName, orientation, bucketId, bucketDisplayName, mediaType, width, height, dataModified, 0, false)
            )
        }

        loaderSuccess(resultList)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        Log.d("ScanTask", "onLoaderReset")
    }

}