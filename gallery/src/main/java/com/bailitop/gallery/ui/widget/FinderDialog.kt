package com.bailitop.gallery.ui.widget

import android.app.Activity
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bailitop.gallery.R
import com.bailitop.gallery.bean.GalleryConfig
import com.bailitop.gallery.loader.IGalleryImageLoader
import com.bailitop.gallery.scan.ScanEntity
import com.bailitop.gallery.ui.adapter.FinderAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 * 目录显示
 */
class FinderDialog (private val activity:Activity,
                    private val galleryConfig: GalleryConfig,
                    private val imageLoader: IGalleryImageLoader?): BottomSheetDialog(activity),
    FinderAdapter.OnFinderItemClickListener {

    private var finderRecyclerView: RecyclerView
    private var finderAdapter: FinderAdapter ?= null
    private val finderList: ArrayList<ScanEntity> = ArrayList()
    private var finderSelectListener: OnFinderSelectListener?= null

    init {
       val view = LayoutInflater.from(activity).inflate(R.layout.layout_finder, null)
        finderRecyclerView = view.findViewById(R.id.rv_finder)
        setContentView(view)
        dismissWithAnimation = true
    }

    fun setFinderData(finderList: List<ScanEntity>, currParentId: Long) {

        this.finderList.clear()
        this.finderList.addAll(finderList)

        //当前选中的
        var position = 0
        for (index in finderList.indices) {
            if (finderList[index].parent == currParentId) {
                position = index
                break
            }
        }

        if (finderAdapter == null) {
            finderAdapter = FinderAdapter(this.finderList, galleryConfig, imageLoader)
            finderAdapter?.currSelection = position
            finderAdapter?.setOnFinderItemClickListener(this)
            finderRecyclerView.layoutManager = LinearLayoutManager(activity)
            finderRecyclerView.adapter = finderAdapter
        }else {
            finderAdapter?.currSelection = position
            finderAdapter?.notifyDataSetChanged()
        }

    }

    override fun onFinderItemClick(position: Int, scanEntity: ScanEntity) {

        val oldPosition = finderAdapter?.currSelection ?: 0
        finderAdapter?.currSelection = position

        finderAdapter?.notifyItemChanged(oldPosition)
        finderAdapter?.notifyItemChanged(position)
        cancel() //调用 cancel 有动画，调用 dismiss 没有动画
        finderSelectListener?.onFinderSelect(scanEntity)

    }


    fun setOnFinderSelectListener(listener: OnFinderSelectListener) {
        finderSelectListener = listener
    }

    interface OnFinderSelectListener{
        fun onFinderSelect(scanEntity: ScanEntity)
    }

}