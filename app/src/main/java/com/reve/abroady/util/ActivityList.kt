package com.reve.abroady.util

import android.app.Activity

// 회원가입 시 사용한 액티비티 일괄 종료
object ActivityList {
    var actList = ArrayList<Activity>()

    fun finishAllActivities() {
        for (i in 0 until actList.size) {
            actList[i].finish()
        }
    }
}