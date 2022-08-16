package com.reve.abroady.data.entity.post

data class OnePostPerBoard (
    var boardName: String,
    var postTitle: String,
    var postContent: String,
    var locationName: String,
    var locationAddress: String,
    var userName: String,
    var writtenTime: String,
    var like: Int,
    var comment: Int,
    var bookmark: Int
)