package com.reve.abroady.data.entity.post

data class NowTrending(
    var boardName : String,
    //var userProfileImage : String,
    var userName : String,
    var writtenTime : String,
    var title : String,
    var content : String,
    var like : Int,
    var comment : Int
)
