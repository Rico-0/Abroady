package com.reve.abroady.data.entity.post

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OnePostSendResult (
    @SerializedName("status")
    @Expose
    var httpStatus : Int,

    @SerializedName("message")
    @Expose
    var message : String
)