package com.reve.abroady.data.entity.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

// 소셜 로그인 시도 시 서버에서 받는 데이터
data class SocialLoginUserData(
    @SerializedName("type")
    @Expose
    var type : String,

    // 로그인 성공 시 유저에게 부여되는 고유 값
    @SerializedName("id")
    @Expose
    var id : Int,

    @SerializedName("accessToken")
    @Expose
    var accessToken : String,

    @SerializedName("refreshToken")
    @Expose
    var refreshToken : String,

    @SerializedName("social")
    @Expose
    var social : String,

    // 가입하지 않았을 시 유저를 구분하는 고유 값
    @SerializedName("uuid")
    @Expose
    var uuid : String,
)
