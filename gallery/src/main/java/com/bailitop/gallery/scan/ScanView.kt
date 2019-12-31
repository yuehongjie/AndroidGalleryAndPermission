package com.bailitop.gallery.scan

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
     * 扫描文件夹成功
     */
    fun onScanSuccess(arrayList: ArrayList<ScanEntity>, finderList: ArrayList<ScanEntity>)

    /**
     * 拍照成功
     */
    fun onCameraSuccess(scanEntity: ScanEntity)

}