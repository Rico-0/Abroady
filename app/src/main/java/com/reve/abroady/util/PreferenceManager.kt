package com.reve.abroady.util

import com.reve.abroady.base.ApplicationClass.Companion.prefs

private const val LOGGED_IN_BEFORE = "logged_in_before"
private const val LOGIN_TYPE = "login_type"
private const val ACCESS_TOKEN = "access_token"
private const val REFRESH_TOKEN = "refresh_token"
private const val USER_ID = "user_id"

object PreferenceManager {

    // 이전에 로그인한 기록이 있는지 검사 (자동 로그인 구현 시 사용)
    var is_loggedIn_before : Boolean
    get() = prefs.getBoolean(LOGGED_IN_BEFORE, false)
    set(value) = prefs.edit().putBoolean(LOGGED_IN_BEFORE, value).apply()

    // 로그인 타입 (구글, 카카오, 네이버, 애플)
    var login_type : String?
        get() = prefs.getString(LOGIN_TYPE, null)
        set(value) = prefs.edit().putString(LOGIN_TYPE, value).apply()

    // 유저 고유 id 값
    var user_id : Int
        get() = prefs.getInt(USER_ID, -1)
        set(value) = prefs.edit().putInt(USER_ID, value).apply()

    // 액세스 토큰
    var access_token : String?
        get() = prefs.getString(ACCESS_TOKEN, null)
        set(value) = prefs.edit().putString(ACCESS_TOKEN, value).apply()

    // 리프레시 토큰
    var refresh_token : String?
        get() = prefs.getString(REFRESH_TOKEN, null)
        set(value) = prefs.edit().putString(REFRESH_TOKEN, value).apply()
}