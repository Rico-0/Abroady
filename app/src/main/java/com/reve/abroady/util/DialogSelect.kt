package com.reve.abroady.util

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.TextView
import com.reve.abroady.R

class DialogSelect(context : Context) {
    private val dialog = Dialog(context)

    private lateinit var dialog_title : TextView
    private lateinit var dialog_content : TextView
    private lateinit var leftBtn : TextView
    private lateinit var rightBtn : TextView

    private lateinit var leftBtnlistener : LeftBtnClickListener
    private lateinit var rightBtnListener : RightBtnClickListener

    interface LeftBtnClickListener {
        fun onClicked()
    }

    interface RightBtnClickListener {
        fun onClicked()
    }

    fun setLeftBtnClickListener(listener : () -> Unit) {
        this.leftBtnlistener = object : LeftBtnClickListener {
            override fun onClicked() {
                listener()
            }
        }
    }

    fun setRightBtnClickListener(listener : () -> Unit) {
        this.rightBtnListener = object : RightBtnClickListener {
            override fun onClicked() {
                listener()
            }
        }
    }

    fun getDialog() : Dialog {
        return dialog
    }

    fun start(title : String, content : String) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // 다이얼로그 타이틀 숨기기
        dialog.setContentView(R.layout.dialog_select)
        dialog.setCancelable(false) // 화면 밖 터치 시 종료 막기

        dialog_title = dialog.findViewById(R.id.dialog_title)
        dialog_content = dialog.findViewById(R.id.dialog_content)
        leftBtn = dialog.findViewById(R.id.left_button)
        rightBtn = dialog.findViewById(R.id.right_button)

        dialog_title.text = title
        dialog_content.text = content

        leftBtn.setOnClickListener {
            leftBtnlistener.onClicked()
        }
        rightBtn.setOnClickListener {
            rightBtnListener.onClicked()
        }
        dialog.show()
    }
}