package com.reve.abroady.model.data.post

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AllPostGetModel(
    @SerializedName("status")
    @Expose
    var httpStatus : Int,

    @SerializedName("message")
    @Expose
    var message : String,

    @SerializedName("data")
    @Expose
    var postList : List<OnePostGetModel>
)
