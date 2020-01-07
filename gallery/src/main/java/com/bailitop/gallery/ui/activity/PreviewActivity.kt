package com.bailitop.gallery.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bailitop.gallery.R
import com.bailitop.gallery.constant.GalleryInnerConstant
import com.bailitop.gallery.ext.statusBarColor
import com.bailitop.gallery.scan.ScanEntity
import com.bailitop.gallery.ui.adapter.PreviewAdapter
import kotlinx.android.synthetic.main.activity_preview_gallery.*
import kotlin.collections.ArrayList

class PreviewActivity : AppCompatActivity() {

    private lateinit var galleryList: ArrayList<ScanEntity>
    private lateinit var selectedList: ArrayList<ScanEntity>
    private var currPosition: Int = 0
    private var total: Int = 0

    private lateinit var adapter: PreviewAdapter
    private lateinit var pageChangeCallback: ViewPager2.OnPageChangeCallback

    /** 已选列表，是否发生了变化（减少相册页面的刷新）*/
    private var isSelectListChange = false
    /** 是否点击了完成按钮，需要销毁相册页面 */
    private var isDoneFinish = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview_gallery)

        //设置状态栏颜色
        window.statusBarColor(ContextCompat.getColor(this, R.color.status_bar_color))

        /** 取数据，优先级：被迫销毁时存储的 -> 传入的 -> 空的 */
        val galleryBundle = savedInstanceState ?: intent.extras ?: Bundle.EMPTY
        //取数据
        galleryList = galleryBundle.getParcelableArrayList<ScanEntity>(GalleryInnerConstant.KEY_GALLERY_LIST) ?: ArrayList()
        selectedList = galleryBundle.getParcelableArrayList<ScanEntity>(GalleryInnerConstant.KEY_SELECT_LIST) ?: ArrayList()
        currPosition = galleryBundle.getInt(GalleryInnerConstant.KEY_CURR_POSITION, 0)
        total = galleryList.size

        initView()
    }

    /** 存储数据，被迫销毁后， 恢复现场用 */
    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        Log.d("PreviewActivity", "onSaveInstanceState")
        outState.apply {
            putParcelableArrayList(GalleryInnerConstant.KEY_GALLERY_LIST, galleryList)
            putParcelableArrayList(GalleryInnerConstant.KEY_SELECT_LIST, selectedList)
            putInt(GalleryInnerConstant.KEY_CURR_POSITION, currPosition)
        }
    }

    private fun initView() {

        adapter = PreviewAdapter(galleryList)
        vpPreview.adapter = adapter
        //注册翻页监听
        pageChangeCallback = object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currPosition = position
                preCheckbox.isChecked = galleryList[position].isCheck
                tvTitle.text = "${position + 1}/$total"
            }
        }

        vpPreview.registerOnPageChangeCallback(pageChangeCallback)

        //防止越界
        if (currPosition < galleryList.size) {
            Log.d("PreviewActivity", "从第 ${currPosition + 1} 张图片预览")
            vpPreview.setCurrentItem(currPosition, false)
        }

        //复选框点击
        preCheckbox.setOnClickListener {
            val currEntity = galleryList[currPosition]
            currEntity.isCheck = !currEntity.isCheck

            //根据 id 查找选中列表中是否已有相同的数据
            val old = selectedList.find { it.id == currEntity.id }

            // 删除旧的 id 相同的数据
            if (old != null) {
                selectedList.remove(old)
            }

            //需要加入新的数据
            if (currEntity.isCheck) {
                selectedList.add(currEntity)
            }

            Log.d("GalleryActivity", "select change: ${selectedList.map { it.id }}")

            isSelectListChange = true

            checkViewEnable()
        }

        //确定按钮点击
        tvSure.setOnClickListener {
            isDoneFinish = true
            onBackPressed()
        }
        //返回按钮点击
        ivBack.setOnClickListener {
            isDoneFinish = false
            onBackPressed()
        }

        checkViewEnable()

    }

    private fun checkViewEnable(){
        val selectedCount = selectedList.size
        if (selectedCount > 0) {
            tvSure.isEnabled = true
            tvSure.text = "确定($selectedCount)"
        }else {
            tvSure.isEnabled = false
            tvSure.text = "确定"
        }
    }

    /**
     * 重写返回，将已选数据带回
     * */
    override fun onBackPressed() {

        val resultIntent = Intent()
        val bundle = Bundle()
        if (isSelectListChange) { //减少数据传递，只有变化时，才传递选中的数据列表
            bundle.putParcelableArrayList(GalleryInnerConstant.KEY_SELECT_LIST, selectedList)
        }
        bundle.putBoolean(GalleryInnerConstant.KEY_IS_SELECT_LIST_CHANGE, isSelectListChange)
        bundle.putBoolean(GalleryInnerConstant.KEY_IS_DONE_FINISH, isDoneFinish)
        resultIntent.putExtras(bundle)
        setResult(GalleryInnerConstant.RESULT_CODE_PREVIEW, resultIntent)

        // super 要写在 setResult 后面，否则无法携带数据返回
        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        vpPreview.unregisterOnPageChangeCallback(pageChangeCallback)
    }
}
