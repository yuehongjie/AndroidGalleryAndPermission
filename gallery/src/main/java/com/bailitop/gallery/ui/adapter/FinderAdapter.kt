package com.bailitop.gallery.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bailitop.gallery.R
import com.bailitop.gallery.ext.display
import com.bailitop.gallery.ext.externalUri
import com.bailitop.gallery.scan.ScanConst
import com.bailitop.gallery.scan.ScanEntity

class FinderAdapter (private val finderList: List<ScanEntity>): RecyclerView.Adapter<FinderAdapter.FinderHolder>() {

    private var itemListener: OnFinderItemClickListener ?= null
    var currSelection: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinderHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_finder, parent, false)
        return FinderHolder(itemView)
    }

    override fun getItemCount(): Int = finderList.size

    override fun onBindViewHolder(holder: FinderHolder, position: Int) {
        val currEntity = finderList[position]
        holder.ivFinderCover.display(currEntity.externalUri())
        holder.tvFinderName.text = if (currEntity.parent == ScanConst.ALL) "全部" else currEntity.bucketDisplayName
        holder.tvPhotoCount.text = "(${currEntity.count})"
        holder.ivSelector.visibility = if (position == currSelection) View.VISIBLE else View.INVISIBLE

        holder.itemView.setOnClickListener {
            itemListener?.onFinderItemClick(position, currEntity)
        }
    }


    fun setOnFinderItemClickListener(listener: OnFinderItemClickListener) {
        this.itemListener = listener
    }

    interface OnFinderItemClickListener{
        fun onFinderItemClick(position: Int, scanEntity: ScanEntity)
    }


    class FinderHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val ivFinderCover: ImageView = itemView.findViewById(R.id.iv_finder_cover)
        val tvFinderName: TextView = itemView.findViewById(R.id.tv_finder_name)
        val tvPhotoCount: TextView = itemView.findViewById(R.id.tv_photo_count)
        val ivSelector: ImageView = itemView.findViewById(R.id.iv_selector)
    }
}