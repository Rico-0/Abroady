package com.reve.abroady.model.data.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

// 회원 가입 완료 시 서버에서 받는 데이터 (유저 데이터 부분)
data class SignUpGetUserData(
    @SerializedName("id")
    @Expose
    var id : Int,

    @SerializedName("nickname")
    @Expose
    var nickname : String,

    @SerializedName("social")
    @Expose
    var social : String,

    @SerializedName("accessToken")
    @Expose
    var accessToken : String,

    @SerializedName("refreshToken")
    @Expose
    var refreshToken : String,

)
