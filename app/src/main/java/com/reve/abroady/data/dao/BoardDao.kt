package com.reve.abroady.data.dao

import com.reve.abroady.data.entity.post.AllPostGetModel
import com.reve.abroady.data.entity.post.OnePostSendModel
import com.reve.abroady.data.entity.post.OnePostSendResult
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BoardDao {
    @GET("posts")
    fun getAllPost(): Single<Response<AllPostGetModel>>

    @POST("posts")
    fun sendPost(@Body post : OnePostSendModel) : Single<Response<OnePostSendResult>>
}