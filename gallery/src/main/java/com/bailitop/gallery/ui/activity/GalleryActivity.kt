package com.bailitop.gallery.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.bailitop.gallery.R
import com.bailitop.gallery.constant.GalleryConstant
import com.bailitop.gallery.ext.statusBarColor
import com.bailitop.gallery.ext.toast
import com.bailitop.gallery.scan.ScanConst
import com.bailitop.gallery.scan.ScanEntity
import com.bailitop.gallery.scan.ScanView
import com.bailitop.gallery.scan.Scanner
import com.bailitop.gallery.ui.adapter.GalleryAdapter
import com.bailitop.gallery.ui.widget.FinderDialog
import com.bailitop.gallery.ui.widget.SimpleGridDivider
import kotlinx.android.synthetic.main.activity_gallery.*
import java.util.*
import kotlin.collections.ArrayList

class GalleryActivity : AppCompatActivity(), ScanView, GalleryAdapter.OnGalleryItemListener,
    FinderDialog.OnFinderSelectListener {

    companion object{
        const val PERMISSION_STORAGE_REQUEST_CODE = 0x04
    }

    /** 选中的列表 */
    private val selectedList = ArrayList<ScanEntity>()
    /** 当前目录下的媒体文件列表 */
    private val galleryList = ArrayList<ScanEntity>()

    private var galleryAdapter: GalleryAdapter ?= null

    private var scanner: Scanner ?= null

    private var spanCount: Int = 4

    private var currScanParent: Long = ScanConst.ALL

    private var finderDialog: FinderDialog ?= null

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
        galleryAdapter?.setOnGalleryItemListener(this)

        checkPermissionAndScan()

        checkViewEnable()
        initListeners()
    }

    private fun initListeners() {
        //返回 不携带数据
        ivBack.setOnClickListener {
            onBackPressed()
        }

        //返回，并携带选中的数据
        tvSure.setOnClickListener {
            resultFinish()
        }

        //预览当前选中的
        tvPreview.setOnClickListener {
            go2Preview(selectedList, selectedList, 0)
        }

        tvShowFinder.setOnClickListener {
            finderDialog?.show()
        }
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
        currScanParent = parent
        scanner?.scan(parent)
    }

    // ------------------------------------- ScanView  Start ----------------------------------------

    override fun onScanSuccess(galleryList: LinkedList<ScanEntity>) {

        mergeEntity(selectedList, galleryList)

        this.galleryList.clear()
        this.galleryList.addAll(galleryList)
        this.galleryAdapter?.notifyDataSetChanged()

    }

    override fun onScanSingleSuccess(scanEntity: ScanEntity?) {

    }

    override fun onRefreshFinders(finderList: LinkedList<ScanEntity>) {

        if (finderDialog == null) {
            finderDialog = FinderDialog(this)
            finderDialog?.setOnFinderSelectListener(this)
        }
        finderDialog?.setFinderData(finderList, currScanParent)
    }

    // ------------------------------------- ScanView End ----------------------------------------

    /**
     * Finder 选中改变回调
     */
    override fun onFinderSelect(scanEntity: ScanEntity) {
        if (scanEntity.parent == currScanParent) return
        //重新扫描选中相册下的照片
        scan(scanEntity.parent)
        tvShowFinder.text = if (scanEntity.parent == ScanConst.ALL) "全部" else scanEntity.bucketDisplayName
    }


    // ------------------------------------- OnGalleryItemListener Start ----------------------------------------

    // 点击相册 Item -> 预览
    override fun onGalleryItemClick(position: Int) {
        go2Preview(selectedList, galleryList, position)
    }

    //选中数量变化
    override fun onSelectedCountChange() {
        checkViewEnable()
    }

    // ------------------------------------- OnGalleryItemListener End ----------------------------------------

    /**
     * 去预览页：
     * 1）点击某个 item 去预览页，传入的数据的是 当前选中的列表 + 当前相册中列表 + 点击的位置
     * 2）点击 预览 按钮去预览页，传入的数据只有 当前选中的列表 + 当前选中的列表 + 0 ，即只预览选中的
     */
    private fun go2Preview(selectList: ArrayList<ScanEntity>, allList: ArrayList<ScanEntity>, position: Int) {
        //预览
        val intent = Intent(this, PreviewActivity::class.java)
        val bundle = Bundle().apply {
            putParcelableArrayList(GalleryConstant.KEY_GALLERY_LIST, allList)
            putParcelableArrayList(GalleryConstant.KEY_SELECT_LIST, selectList)
            putInt(GalleryConstant.KEY_CURR_POSITION, position)
        }

        intent.putExtras(bundle)
        //打开预览，并需要返回值
        startActivityForResult(intent, GalleryConstant.REQUEST_CODE_PREVIEW)
    }

    /**
     * 预览页面回传数据
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //从预览页面回传的数据
        if (requestCode == GalleryConstant.REQUEST_CODE_PREVIEW) {

            val resultBundle = data?.extras ?: Bundle.EMPTY

            val isSelectListChange = resultBundle.getBoolean(GalleryConstant.KEY_IS_SELECT_LIST_CHANGE)
            val isDoneFinish = resultBundle.getBoolean(GalleryConstant.KEY_IS_DONE_FINISH)
            if (isSelectListChange) { //选中的数据变化
                val newSelectList = resultBundle.getParcelableArrayList<ScanEntity>(GalleryConstant.KEY_SELECT_LIST) ?: ArrayList()
                Log.d("GalleryActivity", "newSelectedList: ${newSelectList.map { it.id }}")
                selectedList.clear()
                selectedList.addAll(newSelectList)
            }

            Log.d("GalleryActivity", "isSelectListChange: $isSelectListChange  |   isDoneFinish: $isDoneFinish  |  selectedList: ${selectedList.map { it.id }}")

            //选择完毕，销毁页面
            if (isDoneFinish) {
                resultFinish()
                return

            }else if (isSelectListChange) { //不销毁页面，但需要刷新选中的 item
                mergeEntity(selectedList, galleryList)
                galleryAdapter?.notifyDataSetChanged()
                checkViewEnable()
            }

        }
    }

    /**
     * 携带数据返回上级页面
     */
    private fun resultFinish() {
        val resultIntent = Intent()
        val bundle = Bundle()
        bundle.putParcelableArrayList(GalleryConstant.KEY_SELECT_LIST, selectedList)
        resultIntent.putExtras(bundle)
        setResult(GalleryConstant.RESULT_CODE_GALLERY, resultIntent)
        finish()
    }

    /**
     * 合并 Entity，根据选中的图片，刷新全部列表中选中的状态
     */
    private fun mergeEntity(selectList: List<ScanEntity>, allList: List<ScanEntity>) {
        allList.forEach { item ->
            item.isCheck = selectList.find { it.id == item.id } != null
        }
    }

    private fun checkViewEnable(){

        val selectedCount = selectedList.size
        if (selectedCount > 0) {
            tvPreview.isEnabled = true
            tvSure.isEnabled = true
            tvPreview.text = "预览($selectedCount)"
            tvSure.text = "确定($selectedCount)"
        }else {
            tvPreview.isEnabled = false
            tvSure.isEnabled = false
            tvPreview.text = "预览"
            tvSure.text = "确定"
        }

    }

}
