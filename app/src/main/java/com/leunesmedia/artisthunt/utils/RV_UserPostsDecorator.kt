package com.leunesmedia.artisthunt.utils

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Decorater Class to make sure the grid has equal spacing between its items
 */
class RV_UserPostsDecorator(private val spacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        with(outRect) {
            top = spacing
            bottom = spacing
            left = spacing
            right = spacing
        }
    }
}