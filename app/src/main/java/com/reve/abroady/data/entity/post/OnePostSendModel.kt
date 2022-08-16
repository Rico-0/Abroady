package com.reve.abroady.data.entity.post

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OnePostSendModel(
  /*  @SerializedName("userId")
    @Expose
    var userId : Int, */

    @SerializedName("categoryId")
    @Expose
    var categoryId : Int,

    @SerializedName("title")
    @Expose
    var title : String,

    @SerializedName("content")
    @Expose
    var content : String,

    @SerializedName("isAnonymous")
    @Expose
    var isAnonymous : Boolean

   /* @SerializedName("image")
    @Expose
    var image : String, */
)
