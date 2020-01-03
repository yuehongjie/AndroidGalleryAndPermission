package com.bailitop.gallery.scan

import android.util.ArrayMap
import androidx.fragment.app.FragmentActivity
import androidx.loader.app.LoaderManager

interface ScanView {

    // ------------------------------ 参数提供 --------------------------------

    /**
     * 用来获取媒体加载器 [LoaderManager.getInstance]
     */
    fun getScanContext(): FragmentActivity

    /**
     * 获取已选中的数据
     */
    fun currSelectedList(): ArrayList<ScanEntity>

    /**
     * 获取扫描的媒体类型
     */
    fun currScanType(): Int

    // ------------------------------- 结果回调 ----------------------------------

    /**
     * 扫描文件夹成功，按目录展示数据
     * key 是目录封面图片数据
     * value 是该目录下的图片列表
     */
    fun onScanSuccess(finderMapList: ArrayMap<ScanEntity, List<ScanEntity>>)

    /**
     * 扫描单个媒体成功，如拍照后扫描新的照片
     */
    fun onScanSingleSuccess(scanEntity: ScanEntity?)

    /**
     * （扫描单个媒体成功后）刷新目录
     */
    fun onRefreshFinders(finderMapList: ArrayMap<ScanEntity, List<ScanEntity>>)

}