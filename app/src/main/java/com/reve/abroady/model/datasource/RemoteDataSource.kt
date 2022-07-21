package com.reve.abroady.model.datasource

import com.reve.abroady.model.data.RetrofitInstance
import com.reve.abroady.model.data.post.AllPostGetModel
import com.reve.abroady.model.data.post.OnePostSendModel
import com.reve.abroady.model.data.post.OnePostSendResult
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Response

class RemoteDataSource {

    fun getAllPost() : Single<Response<AllPostGetModel>> {
        return RetrofitInstance.boardDao.getAllPost()
    }

    fun sendPost(post : OnePostSendModel) : Single<Response<OnePostSendResult>> {
        return RetrofitInstance.boardDao.sendPost(post)
    }

}