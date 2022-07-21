package com.reve.abroady.util.recyclerViewItemDecoration

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class DividerMarginItemDecoration (
    private val a_height : Float,
    private val a_color : Int,
    private val direction : String,
    private val margin : Int
) : RecyclerView.ItemDecoration() {

    private val mPaint = Paint()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
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
        outRect.right = margin
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        mPaint.color = a_color
        val left : Float = parent.paddingLeft.toFloat()
        val right : Float = (parent.width - parent.paddingRight).toFloat()
        val childCount = parent.childCount

        for(i in 0 until childCount) {
            val child : View = parent.getChildAt(i)
            val params : RecyclerView.LayoutParams = child.layoutParams as RecyclerView.LayoutParams
            val top : Float = (child.bottom + params.bottomMargin).toFloat()
            val bottom : Float = top + a_height
            c.drawRect(left, top, right, bottom, mPaint)
        }
    }
}