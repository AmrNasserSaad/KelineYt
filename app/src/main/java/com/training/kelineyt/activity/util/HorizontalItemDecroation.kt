package com.training.kelineyt.activity.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

// this for make a some space between items in RV
class HorizontalItemDecoration(private val amount: Int = 16) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.right = amount
    }
}