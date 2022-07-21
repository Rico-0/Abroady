package com.reve.abroady.model.dao

import com.reve.abroady.model.data.post.AllPostGetModel
import com.reve.abroady.model.data.post.OnePostSendModel
import com.reve.abroady.model.data.post.OnePostSendResult
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BoardDao {
    @GET("posts")
    fun getAllPost(): Single<Response<AllPostGetModel>>

    @POST("posts")
    fun sendPost(@Body post : OnePostSendModel) : Single<Response<OnePostSendResult>>
}