package com.reve.abroady.base

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.provider.Telephony.Mms.Part.FILENAME
import com.kakao.sdk.common.KakaoSdk
import com.navercorp.nid.NaverIdLoginSDK
import com.nhn.android.naverlogin.OAuthLogin
import com.reve.abroady.R

class ApplicationClass : Application() {
    companion object {
        var appContext : Context? = null
        lateinit var prefs : SharedPreferences
    }
    override fun onCreate() {
        super.onCreate()
        appContext = this
        // kakao 로그인 초기화
        KakaoSdk.init(this, getString(R.string.kakao_native_key))
        // naver 로그인 초기화
        NaverIdLoginSDK.initialize(this, getString(R.string.naver_client_id), getString(R.string.naver_client_secret), getString(R.string.app_name))
        prefs = getSharedPreferences(FILENAME, 0) // 사용자 설정값을 얻어옴
    }
}