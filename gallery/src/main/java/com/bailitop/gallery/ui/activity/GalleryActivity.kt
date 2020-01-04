package com.bailitop.gallery.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.bailitop.gallery.R
import com.bailitop.gallery.ext.statusBarColor
import com.bailitop.gallery.ext.toast
import com.bailitop.gallery.scan.ScanConst
import com.bailitop.gallery.scan.ScanEntity
import com.bailitop.gallery.scan.ScanView
import com.bailitop.gallery.scan.Scanner
import com.bailitop.gallery.ui.adapter.GalleryAdapter
import com.bailitop.gallery.ui.widget.SimpleGridDivider
import kotlinx.android.synthetic.main.activity_gallery.*
import java.util.*

class GalleryActivity : AppCompatActivity(), ScanView {

    companion object{
        const val PERMISSION_STORAGE_REQUEST_CODE = 0x04
    }

    /** 选中的列表 */
    private val selectedList = LinkedList<ScanEntity>()
    /** 目录列表 */
    private val finderList = LinkedList<ScanEntity>()
    /** 当前目录下的媒体文件列表 */
    private val galleryList = LinkedList<ScanEntity>()

    private var galleryAdapter: GalleryAdapter ?= null

    private var scanner: Scanner ?= null

    private var spanCount: Int = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        //设置状态栏颜色
        window.statusBarColor(ContextCompat.getColor(this, R.color.status_bar_color))

        scanner = Scanner(this, ScanConst.IMAGE, this)

        rvGallery.setHasFixedSize(true)
        rvGallery.layoutManager = GridLayoutManager(this, spanCount)
        rvGallery.addItemDecoration(SimpleGridDivider(4))
        galleryAdapter = GalleryAdapter(galleryList, selectedList, displaySize(spanCount))
        rvGallery.adapter = galleryAdapter

        checkPermissionAndScan()
    }

    /**
     * 按照每行显示的照片数量，设置照片的宽 = 高
     */
    private fun displaySize(count: Int): Int {
        val display = window.windowManager.defaultDisplay
        val dm = DisplayMetrics()
        display.getMetrics(dm)
        return dm.widthPixels / count
    }

    /**
     * 检查权限，并扫描媒体
     */
    private fun checkPermissionAndScan() {
        //检查权限
        val hasPermission = ContextCompat.checkSelfPermission(application, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (hasPermission == PackageManager.PERMISSION_GRANTED) {
            //有权限，扫描
            scan(ScanConst.ALL)
        }else {
            //没有权限，申请权限
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_STORAGE_REQUEST_CODE)
        }
    }

    /**
     * 权限申请结果回调
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_STORAGE_REQUEST_CODE) {
            var isAllGranted = true
            for (grant in grantResults) {
                if (grant == PackageManager.PERMISSION_DENIED) {
                    isAllGranted = false
                    break
                }
            }

            if (isAllGranted) {
                //有权限，扫描
                scan(ScanConst.ALL)
            }else {
                toast("没有权限")
            }
        }
    }

    private fun scan(parent: Long) {
        scanner?.scan(parent)
    }

    // ------------------------------------- ScanView  Start ----------------------------------------

    override fun onScanSuccess(galleryList: LinkedList<ScanEntity>) {
        galleryList.forEach { item ->
            item.isCheck = selectedList.find { it.id == item.id } != null
        }

        this.galleryList.clear()
        this.galleryList.addAll(galleryList)
        this.galleryAdapter?.notifyDataSetChanged()

    }

    override fun onScanSingleSuccess(scanEntity: ScanEntity?) {

    }

    override fun onRefreshFinders(finderList: LinkedList<ScanEntity>) {

    }

    // ------------------------------------- ScanView End ----------------------------------------


}
