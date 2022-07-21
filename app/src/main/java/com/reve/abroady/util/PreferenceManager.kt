package com.reve.abroady.util

import com.reve.abroady.base.ApplicationClass.Companion.prefs

private const val LOGIN_TYPE = "login_type"

object PreferenceManager {
    // 로그인 타입 (구글, 카카오, 네이버, 애플)
    var login_type : String?
        get() = prefs.getString(LOGIN_TYPE, "")
        set(value) = prefs.edit().putString(LOGIN_TYPE, value).apply()
}