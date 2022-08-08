package com.reve.abroady.model.data.login

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

// 회원 가입 완료 시 서버에서 받는 데이터
@Keep
data class SignUpUserData (
    @SerializedName("status")
    @Expose
    var httpStatus : Int,

    @SerializedName("message")
    @Expose
    var message : String,

    @SerializedName("data")
    @Expose
    var userData : SignUpGetUserData
)
