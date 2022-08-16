package com.reve.abroady.data.entity.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RefreshAccessToken (
    @SerializedName("accessToken")
    @Expose
    var token : String
)
