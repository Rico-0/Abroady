package com.reve.abroady.model.data.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RefreshAccessToken (
    @SerializedName("accessToken")
    @Expose
    var token : String
)
