package com.bailitop.gallery.ui.widget

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * 简单的 Grid 分割线
 */
class SimpleGridDivider(private val divider: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        val layoutManager = parent.layoutManager as GridLayoutManager
        val spanCount = layoutManager.spanCount
        val i = position % spanCount
        val first = position / spanCount + 1 == 1
        val top = if (first) divider / 2 else 0
        val left = if (i + 1 == 1) divider else divider / 2
        val right = if (i + 1 == spanCount) divider else divider / 2
        outRect.set(left, top, right, divider)
    }
}