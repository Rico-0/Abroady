package com.reve.abroady.data.entity.post

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OnePostGetModel(
    @SerializedName("id")
    @Expose
    var postId : Int,

    @SerializedName("userId")
    @Expose
    var userId : Int,

    @SerializedName("categoryId")
    @Expose
    var categoryId : Int,
   // var boardName : String,
    // var userProfileImage : String,
   // var userName : String,
  //  var writtenDate : String,

    @SerializedName("title")
    @Expose
    var title : String,

    @SerializedName("content")
    @Expose
    var content : String,

    @SerializedName("isAnonymous")
    @Expose
    var isAnonymous : Boolean,

    @SerializedName("image")
    @Expose
    var image : String,

    @SerializedName("createdAt")
    @Expose
    var createdDate : String,

    @SerializedName("updatedAt")
    @Expose
    var updatedDate : String
    // var imageList : List<String>,
   // var imageCount : Int, // imageList의 사이즈로 추후 변경
   // var like : Int,
   // var comment : Int
)
