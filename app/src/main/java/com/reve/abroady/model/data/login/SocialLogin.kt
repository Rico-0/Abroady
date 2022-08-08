package com.reve.abroady.model.data.login

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

// 소셜 로그인 시 사용되는 데이터 구조
@Keep
data class SocialLogin(
    @SerializedName("status")
    @Expose
    var httpStatus : Int,

    @SerializedName("message")
    @Expose
    var message : String,

    @SerializedName("data")
    @Expose
    var userData : SocialLoginUserData
)

