package com.reve.abroady.model.data

import com.reve.abroady.R
import com.reve.abroady.model.dao.BoardDao
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/* need to add .gitignore
https://yunaaaas.tistory.com/1 */

object RetrofitInstance {

    val API_URL = "http://54.180.92.180:3000/"
    var mRetrofit: Retrofit
    var boardDao: BoardDao

    init {
        mRetrofit = Retrofit.Builder().apply {
            baseUrl(API_URL)
            addConverterFactory(GsonConverterFactory.create())
            addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        }.build()

        boardDao = mRetrofit.create(BoardDao::class.java)
    }

    fun getBoardDaoInstance() : BoardDao {
        return boardDao
    }
}
