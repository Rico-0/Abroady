package com.reve.abroady.data.entity.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

// 토큰 재발급 시 사용되는 데이터 구조
data class AgainToken (
    @SerializedName("status")
    @Expose
    var httpStatus : Int,

    @SerializedName("message")
    @Expose
    var message : String,

    @SerializedName("data")
    @Expose
    var token : RefreshAccessToken
)
