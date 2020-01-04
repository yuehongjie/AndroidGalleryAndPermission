package com.bailitop.gallery.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bailitop.gallery.R
import com.bailitop.gallery.scan.ScanConst
import com.bailitop.gallery.scan.ScanEntity
import com.bailitop.gallery.ui.adapter.vh.CameraViewHodler
import com.bailitop.gallery.ui.adapter.vh.PhotoViewHolder
import java.util.*

class GalleryAdapter(val galleryList: List<ScanEntity>, val selectedList: LinkedList<ScanEntity>, val displaySize: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    companion object{
        /** 显示照片 */
        private const val TYPE_PHOTO = 0
        /** 拍照 */
        private const val TYPE_CAMERA = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            TYPE_CAMERA -> {
                val cameraView = LayoutInflater.from(parent.context).inflate(R.layout.item_camera, parent, false)
                CameraViewHodler(cameraView)
            }
            else -> {
                val photoView = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
                PhotoViewHolder(photoView)
            }
        }
    }

    override fun getItemCount(): Int = galleryList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CameraViewHodler) {
            holder.bindCamera()
        }else if (holder is PhotoViewHolder) {
            holder.bindPhoto(galleryList[position], selectedList, displaySize)
        }
    }

    /**
     * 如果某个位置（一般是第一个）ScanEntity 的 id 是 CAMERA_ID 表示拍照，否则表示显示照片
     */
    override fun getItemViewType(position: Int): Int {
        return if (galleryList[position].id == ScanConst.CAMERA_ID) TYPE_CAMERA else TYPE_PHOTO
    }

}