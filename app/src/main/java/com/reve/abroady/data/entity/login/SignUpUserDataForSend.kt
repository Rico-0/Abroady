package com.reve.abroady.data.entity.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

// 회원 가입을 위해 서버로 데이터를 보낼 때 사용
data class SignUpUserDataForSend(
    @SerializedName("social")
    @Expose
    var social : String,

    @SerializedName("uuid")
    @Expose
    var uuid : String,

    @SerializedName("nickname")
    @Expose
    var nickname : String,

    @SerializedName("country")
    @Expose
    var country : String,

    @SerializedName("language")
    @Expose
    var language : List<String>
)
