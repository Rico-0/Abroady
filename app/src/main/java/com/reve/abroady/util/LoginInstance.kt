package com.reve.abroady.util

import android.app.Activity
import android.content.Context
import com.reve.abroady.ui.login.classfile.GoogleLogin
import com.reve.abroady.ui.login.classfile.KakaoLogin
import com.reve.abroady.ui.login.classfile.LoginBase
import com.reve.abroady.ui.login.classfile.NaverLogin

object LoginInstance {
    // 로그인 타입에 따라 로그인 객체를 할당받는다.
    fun getLoginInstance(context : Context, activity : Activity, loginType : String) : LoginBase {
        if (loginType.equals("kakao"))
           return KakaoLogin(activity)
        else if (loginType.equals("google"))  // 추후 수정
            return GoogleLogin(activity)
        else if (loginType.equals("naver"))
            return NaverLogin(context, activity)
        else // TODO : apple, naver login
            return KakaoLogin(activity)
    }
}