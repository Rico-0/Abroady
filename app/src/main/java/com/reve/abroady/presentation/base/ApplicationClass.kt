package com.reve.abroady.presentation.base

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.provider.Telephony.Mms.Part.FILENAME
import com.kakao.sdk.common.KakaoSdk
import com.navercorp.nid.NaverIdLoginSDK
import com.reve.abroady.R
import com.reve.abroady.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class ApplicationClass : Application() {
    companion object {
        var appContext : Context? = null
        lateinit var prefs : SharedPreferences
    }
    override fun onCreate() {
        super.onCreate()
        appContext = this

        // 의존성 주입
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@ApplicationClass)
            modules(appModule)
        }

        // kakao 로그인 초기화
        KakaoSdk.init(this, getString(R.string.kakao_native_key))
        // naver 로그인 초기화
        NaverIdLoginSDK.initialize(this, getString(R.string.naver_client_id), getString(R.string.naver_client_secret), getString(R.string.app_name))
        prefs = getSharedPreferences(FILENAME, 0) // 사용자 설정값을 얻어옴
    }
}