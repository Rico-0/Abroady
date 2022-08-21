package com.reve.abroady.util

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.TextView
import com.reve.abroady.R

// 사용자에게 알림을 띄워 주는 다이얼로그
class DialogAlert(context : Context) {

    private val dialog = Dialog(context)

    private lateinit var dialog_title : TextView
    private lateinit var dialog_content : TextView
    private lateinit var acceptBtn : TextView

    fun start(title : String, content : String) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // 다이얼로그 타이틀 숨기기
        dialog.setContentView(R.layout.dialog_alert)
        dialog.setCancelable(false) // 화면 밖 터치 시 종료 막기

        dialog_title = dialog.findViewById(R.id.dialog_title)
        dialog_content = dialog.findViewById(R.id.dialog_content)

        dialog_title.text = title
        dialog_content.text = content

        acceptBtn = dialog.findViewById(R.id.dialog_accept_button)
        acceptBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}