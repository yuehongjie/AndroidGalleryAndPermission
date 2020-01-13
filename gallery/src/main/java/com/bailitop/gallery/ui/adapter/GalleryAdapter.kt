package com.bailitop.gallery.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bailitop.gallery.R
import com.bailitop.gallery.ext.externalUri
import com.bailitop.gallery.loader.IGalleryImageLoader
import com.bailitop.gallery.scan.ScanEntity
import java.util.*

class GalleryAdapter(
    private val galleryList: List<ScanEntity>,
    private val selectedList: ArrayList<ScanEntity>,
    private val displaySize: Int,
    private val maxSelectedCount: Int,
    private val imageLoader: IGalleryImageLoader?) : RecyclerView.Adapter<GalleryAdapter.PhotoViewHolder>(){

    private var galleryItemListener: OnGalleryItemListener ?= null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {

        val photoView = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return PhotoViewHolder(photoView)

    }

    override fun getItemCount(): Int = galleryList.size

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {

        val scanEntity = galleryList[position]

        //holder.ivPhoto.display(scanEntity.externalUri(), displaySize)
        imageLoader?.displayGalleryImage(holder.ivPhoto, scanEntity.externalUri(), displaySize)

        holder.viewMask.visibility = if (scanEntity.isCheck) View.VISIBLE else View.INVISIBLE

        holder.checkBox.isChecked = scanEntity.isCheck
        holder.checkBox.setOnClickListener {

            //根据 id 查找选中列表中是否已有相同的数据
            val old = selectedList.find { it.id == scanEntity.id }

            // 删除旧的 id 相同的数据
            if (old != null) {
                selectedList.remove(old)
            }else {
                //想添加新的，先检查是否查过最大数
                if (selectedList.size >= maxSelectedCount) {
                    holder.checkBox.isChecked = false
                    galleryItemListener?.onGalleryMaxCount(maxSelectedCount)
                    return@setOnClickListener
                }

            }

            scanEntity.isCheck = !scanEntity.isCheck

            //如果选中，则添加新的
            if (scanEntity.isCheck) {
                selectedList.add(scanEntity)
            }

            holder.viewMask.visibility = if (scanEntity.isCheck) View.VISIBLE else View.INVISIBLE

            //选中数量变化回调
            galleryItemListener?.onSelectedCountChange()
        }

        //预览
        holder.itemView.setOnClickListener {
            galleryItemListener?.onGalleryItemClick(position)
        }
    }

    fun setOnGalleryItemListener(listener: OnGalleryItemListener) {
        galleryItemListener = listener
    }

    interface OnGalleryItemListener {
        /** item 点击 */
        fun onGalleryItemClick(position: Int)
        /** 选中数量变化 */
        fun onSelectedCountChange()
        /** 已达到最大可选数量 */
        fun onGalleryMaxCount(maxCount: Int)

    }

    /**
     * 显示照片的 ViewHolder
     */
    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val ivPhoto: ImageView = itemView.findViewById(R.id.ivPhoto)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkbox)
        val viewMask: View = itemView.findViewById(R.id.viewMask)

    }

}