package com.reve.abroady.ui.login.classfile

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.reve.abroady.util.PreferenceManager.is_loggedIn_before
import com.reve.abroady.util.PreferenceManager.login_type

abstract class LoginBase {
    abstract fun checkAlreadyLoggedIn()
    fun alreadyHaveAccount(activity: Activity) {
        // 다른 SNS로 로그인한 이력이 있는 경우
        val alterDialog = AlertDialog.Builder(activity)
            .setMessage("이미 다른 SNS로 가입된 아이디가 존재합니다. 해당 SNS로 로그인해주세요.")
            .setPositiveButton("확인") { dialog, which ->
                dialog?.run {
                    dismiss()
                }
            }
        alterDialog.show()
    }

    // 로그인 버튼 눌렀을 때 호출
    abstract fun login()
    abstract fun logout()
    abstract fun deleteAccount()
    abstract fun getUserInfo()
    // 로그인 API 토큰 가지고 백엔드와 연동하여 서버 측 회원가입 수행
    abstract fun signUp(token : String)
    // 로그인 API 토큰 가지고 백엔드와 연동하여 서버 측 로그인 수행
    abstract fun signIn(token : String)

    companion object {
        const val LOGIN_SUCCESS = 200
        const val SIGNUP_SUCCESS = 201
        const val NOT_SIGNED_UP = 202
        const val LOGIN_FAIL = 400
        const val UNAUTHORIZED = 401
        const val ALREADY_HAVE_SAME_NICKNAME = 409
        const val SERVER_ERROR = 500
    }
}