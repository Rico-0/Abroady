package com.reve.abroady.data.repository

import com.reve.abroady.data.RetrofitInstance
import com.reve.abroady.data.entity.post.AllPostGetModel
import com.reve.abroady.data.entity.post.OnePostSendModel
import com.reve.abroady.data.entity.post.OnePostSendResult
import io.reactivex.Single
import retrofit2.Response

class RemoteRepository {

    fun getAllPost() : Single<Response<AllPostGetModel>> {
        return RetrofitInstance.board_Dao.getAllPost()
    }

    fun sendPost(post : OnePostSendModel) : Single<Response<OnePostSendResult>> {
        return RetrofitInstance.board_Dao.sendPost(post)
    }

}