package com.andry.adapter

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CategoryItemDecoration(
    context: Context,
) : RecyclerView.ItemDecoration() {

    private val spacing: Int = context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._8sdp)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        // Apply spacing to all items except the first one
        outRect.top = spacing
        outRect.left= spacing
        outRect.right = spacing
    }
}
