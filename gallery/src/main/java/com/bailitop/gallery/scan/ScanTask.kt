package com.bailitop.gallery.scan

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.bailitop.gallery.scan.args.Columns

/**
 * 扫描图片要执行的任务
 * loaderSuccess：扫描成功后，调用的方法，是一个高阶函数
 */
class ScanTask(private val context: Context, private val loaderSuccess: (ArrayList<ScanEntity>) -> Unit):LoaderManager.LoaderCallbacks<Cursor> {

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
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
            scanSingleFile != 0L ->{}
            //扫描全部
            scanParent == ScanConst.ALL -> {}
            //扫描某个文件夹
            else -> {}
        }

        return CursorLoader(
            context,

        )
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}