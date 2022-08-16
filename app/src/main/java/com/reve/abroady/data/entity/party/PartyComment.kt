package com.reve.abroady.data.entity.party

// 추후 수정
data class PartyComment(
    var userProfileImage : String,
    var userName : String,
    var writtenTime : String,
    var content : String,
    var isHaveRootComment : Boolean
)
