package com.bailitop.gallery.scan


import android.content.Context
import android.os.Bundle
import androidx.loader.app.LoaderManager
import com.bailitop.gallery.scan.args.Columns

/**
 * 图库扫描工具
 */
class Scanner(private val scanView: ScanView) {

    companion object {
        private const val SCAN_LOADER_ID = 0x111
    }

    /**
     * 资源加载器，和 LifeCycle(如 FragmentActivity) 生命周期自动绑定
     */
    private val loaderManager: LoaderManager = LoaderManager.getInstance(scanView.getScanContext())

    /**
     * 文件夹列表
     */
    private val finderList = ArrayList<ScanEntity>()

    /**
     * 扫描某个文件夹下图片 or 视频
     */
    fun scan(parent: Long){
        //当前有正在扫描的任务
        if (loaderManager.hasRunningLoaders()) return

        loaderManager.restartLoader(
            SCAN_LOADER_ID,
            Bundle().apply {
                putLong(Columns.PARENT, parent)
                putInt(Columns.SCAN_TYPE, scanView.currScanType())
            },

        )
    }

}