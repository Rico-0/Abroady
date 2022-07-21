package com.reve.abroady.util.recyclerViewItemDecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

// 리사이클러뷰 여백 설정
class MarginItemDecoration(private val direction: String, private val margin: Int) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        if (position != 0) { // 첫 번째 아이템이 아닐 때만 여백 추가
            when (direction) {
                "top" -> {
                    outRect.top = margin
                }
                "bottom" -> {
                    outRect.bottom = margin
                }
                "left" -> {
                    outRect.left = margin
                }
                "right" -> {
                    outRect.right = margin
                }
            }
        }
    }
}