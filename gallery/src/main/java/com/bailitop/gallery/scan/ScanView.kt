package com.bailitop.gallery.scan

import java.util.*

interface ScanView {

    /**
     * 扫描成功，结果为当前扫描的目录下的数据
     */
    fun onScanSuccess(galleryList: LinkedList<ScanEntity>)

    /**
     * 扫描单个媒体成功，如拍照后扫描新的照片
     */
    fun onScanSingleSuccess(scanEntity: ScanEntity?)

    /**
     * 刷新目录
     */
    fun onRefreshFinders(finderList: LinkedList<ScanEntity>)

}