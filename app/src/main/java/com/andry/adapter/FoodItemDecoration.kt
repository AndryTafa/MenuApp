package com.andry.adapter

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class FoodItemDecoration(
    context: Context,
) : RecyclerView.ItemDecoration() {

    private val spacing: Int = context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._8sdp)

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        // Apply spacing to all items except the first one
        outRect.bottom = spacing
        if (parent.getChildAdapterPosition(view) == 0 || parent.getChildAdapterPosition(view) == 1) {
            outRect.top = spacing
        }
        if (parent.getChildAdapterPosition(view) % 2 == 0) {
            outRect.left = spacing / 2
            outRect.right = spacing
        } else {
            outRect.left = spacing
            outRect.right = spacing / 2

        }
    }
}
