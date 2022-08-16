package com.reve.abroady.data.dao

import io.reactivex.Observable
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import retrofit2.http.*
import java.util.regex.Pattern

class FireBaseLoginDao {

    private val TAG = this.javaClass.simpleName

    companion object {
        private val auth = FirebaseAuth.getInstance()
        private const val EMAIL_ALREADY_EXIST = "The email address is already in use by another account."
    }

    // issue : result 값 세팅이 안 됨
    fun register(email: String, password: String) : Observable<Any> {
        var result : Any? = null
        auth?.createUserWithEmailAndPassword(email, password) // 파이어베이스에 유저를 이메일과 비밀번호로 생성
            ?.addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) { // 정상적으로 이메일과 비밀번호가 전달되어 새 유저 계정을 생성 + 서버 DB에 저장 완료 + 로그인 됨
                    result = authResult.result // 파이어베이스 인증 결과를 저장
                } else if (authResult.exception?.message.equals(EMAIL_ALREADY_EXIST)){ // 이미 DB에 해당 이메일과 패스워드가 있는 경우
                    Log.e(TAG, "이미 존재하는 이메일로 가입 시도")
                    result = EMAIL_ALREADY_EXIST // 이미 이메일이 존재한다는 에러 메시지 저장
                } else {
                    Log.e(TAG, "기타 에러")
                    result = authResult.exception?.message // 다른 에러 메시지 저장
                }
            }.addOnFailureListener {
                Log.e(TAG, "파이어베이스 회원 가입 중 오류 발생 : ${it}")
                result = it.toString()
            }
        return Observable.just(result)
    }

    fun logout() {
        auth.signOut()
    }

    fun login(email: String, password: String) : Observable<Any> {
        var task : AuthResult? = null
        auth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener {  //통신 완료가 된 후 무슨일을 할지
                    result ->
                if (result.isSuccessful) {
                    task = result?.result
                    return@addOnCompleteListener
                } else {
                    // 오류가 난 경우!
                    Log.e(TAG, "파이어베이스 로그인 중 오류 발생 : ${result.exception?.message}")
                    task = result?.result
                }
            }
      return Observable.just(task)
    }

   /* // 토큰 재발급 (이미 로그인한 사용자의 토큰이 만료될 시 사용해야 함)
    @GET("auth/token")
    fun getTokenAgain(@Header("accesstoken") accessToken : String, @Header("refreshtoken") refreshToken : String): Call<AgainToken>

    // 소셜 로그인
    @GET("auth/{social}")
    fun socialLogin(@Path("social") social : String, @Header("token") token : String) : Call<SocialLogin>

    // 회원가입
    @POST("auth")
    fun signUp(@Body userData : SignUpUserDataForSend) : Call<SignUpUserData> */

}