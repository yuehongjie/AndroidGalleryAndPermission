package com.bailitop.gallery.ui.adapter.vh

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bailitop.gallery.R
import com.bailitop.gallery.ext.display
import com.bailitop.gallery.ext.externalUri
import com.bailitop.gallery.scan.ScanEntity
import java.util.*

/**
 * 显示照片的 ViewHolder
 */
class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    private val ivPhoto: ImageView = itemView.findViewById(R.id.ivPhoto)
    private val checkBox: CheckBox = itemView.findViewById(R.id.checkbox)
    private val viewMask: View = itemView.findViewById(R.id.viewMask)

    fun bindPhoto(scanEntity: ScanEntity, selectedList: LinkedList<ScanEntity>, displaySize: Int) {
        ivPhoto.display(scanEntity.externalUri(), displaySize)

        viewMask.visibility = if (scanEntity.isCheck) View.VISIBLE else View.INVISIBLE

        checkBox.isChecked = scanEntity.isCheck
        checkBox.setOnClickListener {

            scanEntity.isCheck = !scanEntity.isCheck

            if (scanEntity.isCheck) {
                selectedList.add(scanEntity)
            }else {
                selectedList.remove(scanEntity)
            }

            viewMask.visibility = if (scanEntity.isCheck) View.VISIBLE else View.INVISIBLE
        }
    }

}