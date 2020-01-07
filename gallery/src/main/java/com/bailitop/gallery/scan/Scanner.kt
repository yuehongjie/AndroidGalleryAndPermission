package com.bailitop.gallery.scan

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.loader.app.LoaderManager
import com.bailitop.gallery.scan.args.Columns
import java.util.*

/**
 * 图库扫描工具
 */
class Scanner(private val context: FragmentActivity, private val scanType:Int, private val scanView: ScanView) {

    companion object {
        private const val SCAN_LOADER_ID = 0x111
    }

    /**
     * 资源加载器，和 LifeCycle(如 FragmentActivity) 生命周期自动绑定
     */
    private val loaderManager: LoaderManager = LoaderManager.getInstance(context)

    /**
     * 目录 集合
     */
    private val finderList = LinkedList<ScanEntity>()

    /**
     * 扫描某个文件夹下图片 or 视频，每次切换目录重新扫描，达到立刻更新的效果
     */
    fun scan(parent: Long){

        Log.d("Scanner", "scan start time: ${System.currentTimeMillis()}")

        //当前有正在扫描的任务
        if (loaderManager.hasRunningLoaders()) return

        //加载资源，会异步执行
        loaderManager.restartLoader(
            SCAN_LOADER_ID,
            Bundle().apply {
                //传参数给 ScanTask 即加载器
                putLong(Columns.PARENT, parent)
                putInt(Columns.SCAN_TYPE, scanType)
            },
            ScanTask(context){
                //加载成功的回调
                Log.d("Scanner", "scan ScanTask end time: ${System.currentTimeMillis()}")
                //如果是扫描全部文件，顺便生成目录
                if (parent == ScanConst.ALL) {
                    createFinders(it)
                    scanView.onRefreshFinders(finderList)
                }

                //扫描成功
                scanView.onScanSuccess(it)
                //释放资源
                loaderManager.destroyLoader(SCAN_LOADER_ID)

                Log.d("Scanner", "scan end time: ${System.currentTimeMillis()}")
            }

        )
    }

    /**
     * 扫描单个（媒体）文件
     */
    fun scanSingleFile(id: Long) {
        loaderManager.restartLoader(
            SCAN_LOADER_ID,
            Bundle().apply {
                //传参数给 ScanTask 即加载器
                putLong(Columns.ID, id)
                putInt(Columns.SCAN_TYPE, scanType)
            },
            ScanTask(context){
                //扫描成功
                val singleEntity = if (it.isNullOrEmpty()) null else it.first()
                //扫描单个文件成功，刷新目录
                refreshFinderAfterSingleScan(singleEntity)
                scanView.onRefreshFinders(finderList)

                scanView.onScanSingleSuccess(singleEntity)
                //释放资源
                loaderManager.destroyLoader(SCAN_LOADER_ID)
            }
        )
    }

    /**
     * 生成目录
     */
    private fun createFinders(list: LinkedList<ScanEntity>){

        //清空旧数据
        finderList.clear()

        // 先插入 “全部” 这个目录，封面为扫描出的第一张图片，数量为全部图片数量
        val finderAll = list.first().copy(parent = ScanConst.ALL, count = list.size)
        finderList.add(finderAll)

        //再按照 parent 目录分组
        val parentMap = list.groupBy { it.parent }
        //遍历分组，取每组第一个元素，作为目录封面元素
        parentMap.forEach {
            val entityList = it.value
            val first = entityList.first().copy(count = entityList.size) //count = 同一目录下的照片数量
            finderList.add(first)
        }

        Log.d("Scanner", "finderList: ${finderList.map { "${it.bucketDisplayName} : ${it.count} | " }}")

    }

    /**
     * 扫描单个媒体成功，则刷新目录
     */
    private fun refreshFinderAfterSingleScan(singleEntity: ScanEntity?){
        if (singleEntity == null) return

        //如果目录是空的，则重新创建
        if (finderList.isEmpty()) {
            //创建“全部”目录
            finderList.add(singleEntity.copy(parent = ScanConst.ALL, count = 1))
            //创建自身所在目录
            finderList.add(singleEntity.copy(count = 1))
        }else {

            finderList.forEach {
                //修改“全部”目录封面和所在目录封面
                if (it.parent == ScanConst.ALL || it.parent == singleEntity.parent) {

                    //前提是修改时间 晚于 封面的时间，因为我们是以修改时间倒叙查询的
                    if (singleEntity.dataModified > it.dataModified) {
                        it.id = singleEntity.id
                        it.size = singleEntity.size
                        it.duration = singleEntity.duration
                        it.parent = if (it.parent == ScanConst.ALL) ScanConst.ALL else singleEntity.parent
                        it.mimeType = singleEntity.mimeType
                        it.displayName = singleEntity.displayName
                        it.orientation = singleEntity.orientation
                        it.bucketId = singleEntity.bucketId
                        it.bucketDisplayName = singleEntity.bucketDisplayName
                        it.mediaType = singleEntity.mediaType
                        it.width = singleEntity.width
                        it.height = singleEntity.height
                        it.dataModified = singleEntity.dataModified
                        it.count = it.count + 1 // 数量 + 1
                        it.isCheck = singleEntity.isCheck
                    }

                }
            }

            //没有找到自身所在的目录，则创建
            if (finderList.find { it.parent == singleEntity.parent } == null) {
                //创建自身所在目录
                finderList.add(singleEntity.copy(count = 1))
            }
        }
    }

}