package com.reve.abroady.model.dao

import com.reve.abroady.model.data.login.AgainToken
import com.reve.abroady.model.data.login.SignUpUserData
import com.reve.abroady.model.data.login.SignUpUserDataForSend
import com.reve.abroady.model.data.login.SocialLogin
import retrofit2.Call
import retrofit2.http.*

interface LoginDao {

    // 토큰 재발급 (이미 로그인한 사용자의 토큰이 만료될 시 사용해야 함)
    @GET("auth/token")
    fun getTokenAgain(@Header("accesstoken") accessToken : String, @Header("refreshtoken") refreshToken : String): Call<AgainToken>

    // 소셜 로그인
    @GET("auth/{social}")
    fun socialLogin(@Path("social") social : String, @Header("token") token : String) : Call<SocialLogin>

    // 회원가입
    @POST("auth")
    fun signUp(@Body userData : SignUpUserDataForSend) : Call<SignUpUserData>

}