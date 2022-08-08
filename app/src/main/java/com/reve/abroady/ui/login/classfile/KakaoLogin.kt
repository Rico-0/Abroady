package com.reve.abroady.ui.login.classfile

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.reve.abroady.model.data.RetrofitInstance
import com.reve.abroady.model.data.login.AgainToken
import com.reve.abroady.model.data.login.SignUpUserData
import com.reve.abroady.model.data.login.SignUpUserDataForSend
import com.reve.abroady.model.data.login.SocialLogin
import com.reve.abroady.ui.MainActivity
import com.reve.abroady.ui.login.LoginSelectActivity
import com.reve.abroady.util.PreferenceManager.access_token
import com.reve.abroady.util.PreferenceManager.is_loggedIn_before
import com.reve.abroady.util.PreferenceManager.login_type
import com.reve.abroady.util.PreferenceManager.refresh_token
import com.reve.abroady.util.PreferenceManager.user_id
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KakaoLogin(private val activity: Activity) : LoginBase() {

    private val TAG: String = this.javaClass.simpleName

    companion object {
        private val loginDao = RetrofitInstance.getLoginDao()
        private const val KAKAO = "kakao"
        private const val SUCCESS_GET_TOKEN_AGAIN = 200
        private const val VALID_TOKEN = 202
        private const val FAIL_GET_TOKEN_AGAIN = 400
        private const val INVALID_TOKEN = 401
    }

    // 카카오 로그인 후 결과 코드
    private val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            when {
                error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                    Toast.makeText(
                        activity,
                        "개인 정보 제공을 취소하였습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                    Toast.makeText(activity, "유효하지 않은 앱입니다.", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                    Toast.makeText(activity, "인증 수단이 유효하지 않아 인증할 수 없는 상태입니다.", Toast.LENGTH_SHORT)
                        .show()
                }
                error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                    Toast.makeText(activity, "요청 파라미터에 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                    Toast.makeText(activity, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                    Toast.makeText(activity, "해시 키 설정이 올바르지 않습니다.", Toast.LENGTH_SHORT)
                        .show()
                }
                error.toString() == AuthErrorCause.ServerError.toString() -> {
                    Toast.makeText(activity, "카카오 서버에 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                    Toast.makeText(activity, "앱에 요청 권한이 없습니다.", Toast.LENGTH_SHORT).show()
                }
                error.toString() == ClientErrorCause.Cancelled.toString() -> {
                    Toast.makeText(activity, "카카오 로그인을 취소하였습니다.", Toast.LENGTH_SHORT).show()
                    activity.finish()
                }
                else -> { // 에러 처리에 안 걸림.....
                   Toast.makeText(activity, "카카오 로그인 중 에러가 발생하였습니다 : ${error} , ${ClientErrorCause.Cancelled.toString()}", Toast.LENGTH_LONG).show()
                }
            }
        } else if (token != null) {
            Log.d("KakaoLogin", "로그인 성공 : 액세스 토큰 값 -> ${token.accessToken}")
            // 만약 전에 로그인한 기록이 없다면 회원가입 진행
            if (!is_loggedIn_before)
                signUp(token.accessToken)
            else
                signIn(token.accessToken)
        }
    }

    override fun signUp(kakaoToken : String) {
        loginDao.socialLogin(KAKAO, kakaoToken).enqueue(object : Callback<SocialLogin> {
            override fun onResponse(call: Call<SocialLogin>, response: Response<SocialLogin>) {
                if (response.isSuccessful) {
                    Log.d(TAG, "signup status code : ${response.body()}")
                    val userData = response.body()?.userData
                    when (response.body()?.httpStatus) {
                        // 임시 코드 (삭제 예정)
                        LOGIN_SUCCESS -> {
                            val userData = response.body()?.userData
                            userData?.run {
                                user_id = userData.id // 유저 식별 값
                                login_type = KAKAO // 로그인 유형
                                access_token = userData.accessToken
                                refresh_token = userData.refreshToken
                                is_loggedIn_before =
                                    true // 로그인했는지 여부를 true로 변경
                                val intent = Intent(
                                    activity,
                                    MainActivity::class.java
                                )
                                activity.startActivity(
                                    intent.addFlags(
                                        Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    )
                                ) // 스택에 있던 액티비티들을 지우는 역할
                                activity.finish()
                            }
                        }
                        NOT_SIGNED_UP -> { // 회원으로 가입되지 않은 유저인 경우
                            val social = userData?.social
                            val uuid = userData?.uuid
                            if (social != null && uuid != null) { // 소셜 로그인에서 받은 social, uuid 정보를 받아 회원가입을 진행
                                // 닉네임, 국적, 언어는 추후 UI에서 선택한 값으로 받아와야 함 (테스트용)
                                loginDao.signUp(
                                    SignUpUserDataForSend(
                                        social,
                                        uuid,
                                        "test user",
                                        "Korea",
                                        listOf("Korean")
                                    )
                                ).enqueue(object : Callback<SignUpUserData> {
                                    override fun onResponse(
                                        call: Call<SignUpUserData>,
                                        response: Response<SignUpUserData>
                                    ) {
                                        if (response.isSuccessful) {
                                            when (response.body()?.httpStatus) {
                                                SIGNUP_SUCCESS -> { // 회원 가입 성공
                                                    // 서버에서 반환해 준 데이터를 SharedPreference에 저장
                                                    val userData = response.body()?.userData
                                                    userData?.run {
                                                        user_id = userData.id // 유저 식별 값
                                                        login_type = KAKAO // 로그인 유형
                                                        access_token = userData.accessToken
                                                        refresh_token = userData.refreshToken
                                                        is_loggedIn_before =
                                                            true // 로그인했는지 여부를 true로 변경
                                                        val intent = Intent(
                                                            activity,
                                                            MainActivity::class.java
                                                        )
                                                        activity.startActivity(
                                                            intent.addFlags(
                                                                Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                            )
                                                        ) // 스택에 있던 액티비티들을 지우는 역할
                                                        activity.finish()
                                                    }
                                                }
                                                LOGIN_FAIL -> {
                                                    Log.e(TAG, "잘못된 요청 값 : signUp")
                                                }
                                                ALREADY_HAVE_SAME_NICKNAME -> {
                                                    Log.e(TAG, "이미 사용중인 닉네임 사용")
                                                }
                                                SERVER_ERROR -> {
                                                    Log.e(TAG, "서버 내부 오류 : signUp")
                                                }
                                                else -> {
                                                    Log.d(TAG, "signup else : ${response.errorBody()?.string()}")
                                                }
                                            }
                                        } else {
                                            Log.d(TAG, "signup : ${response.errorBody()?.string()}")
                                        }
                                    }

                                    override fun onFailure(
                                        call: Call<SignUpUserData>,
                                        t: Throwable
                                    ) {
                                        Log.e(TAG, "signup - sign error : $t")
                                    }

                                })
                            }
                        }
                        LOGIN_FAIL -> {
                            Log.e(TAG, "잘못된 요청 값 : socialLogin")
                        }
                        UNAUTHORIZED -> {
                            Log.e(TAG, "인증되지 않은 요청 : socialLogin")
                        }
                        SERVER_ERROR -> {
                            Log.e(TAG, "서버 내부 오류 : socialLogin")
                        }
                    }
                }
            }
            override fun onFailure(call: Call<SocialLogin>, t: Throwable) {
                Log.e(TAG, "signup - socialLogin error : $t")
            }
        })
    }

    override fun signIn(kakaoToken : String) {
        loginDao.socialLogin(KAKAO, kakaoToken).enqueue(object : Callback<SocialLogin> {
            override fun onResponse(call: Call<SocialLogin>, response: Response<SocialLogin>) {
                if (response.isSuccessful) {
                    when (response.body()?.httpStatus) {
                        LOGIN_SUCCESS -> {
                            val userData = response.body()?.userData
                            userData?.run {
                                access_token = userData.accessToken
                                refresh_token = userData.refreshToken
                                val intent = Intent(
                                    activity,
                                    MainActivity::class.java
                                )
                                activity.startActivity(
                                    intent.addFlags(
                                        Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    )
                                ) // 스택에 있던 액티비티들을 지우는 역할
                                activity.finish()
                            }
                        }
                        LOGIN_FAIL -> {
                            Log.e(TAG, "잘못된 요청 값 : signIn")
                        }
                        UNAUTHORIZED -> {
                            Log.e(TAG, "인증되지 않은 요청 : signIn")
                        }
                        SERVER_ERROR -> {
                            Log.e(TAG, "서버 내부 오류 : signIn")
                        }
                        else -> {  Log.d(TAG, "signin else : ${response.errorBody()?.string()}") }
                    }
                } else {
                    Log.d(TAG, "signin : ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<SocialLogin>, t: Throwable) {
                Log.e(TAG, "signin error : $t")
            }
        })
    }

    override fun checkAlreadyLoggedIn() {
        if (AuthApiClient.instance.hasToken()) { // 사용자가 로그인 상태임을 보장하지 않으므로, 추후 백엔드와 연동하여 로그인 기능 구현 필요
            // 현재 로그인한 사용자의 엑세스 토큰 정보 보기
            // me 에서 제공되는 다양한 사용자 정보 없이 가볍게 토큰의 유효성을 체크하는 용도로 추천. 액세스 토큰이 만료된 경우 자동으로 갱신된 새로운 액세스 토큰 정보 반환
            UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                if (error != null) {
                    Log.d(TAG, "카카오 토큰 확인 중 에러 발생 : $error")
                    return@accessTokenInfo
                } else if (tokenInfo != null) { // 유효한 토큰 존재
                    // 토큰 재발급 api로 토큰 유효한지 검사 후 유효하면 메인 화면으로 이동, 아니면 재발급 받고 이동
                        if (access_token != null && refresh_token != null) {
                            loginDao.getTokenAgain(access_token!!, refresh_token!!).enqueue(object : Callback<AgainToken> {
                                override fun onResponse(
                                    call: Call<AgainToken>,
                                    response: Response<AgainToken>
                                ) {
                                    if (response.isSuccessful) {
                                        when (response.body()?.httpStatus) {
                                            SUCCESS_GET_TOKEN_AGAIN -> { // 토큰 재발급 성공
                                                access_token = response.body()?.token?.token
                                                val intent = Intent(activity, MainActivity::class.java)
                                                activity.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                                                activity.finish()
                                            }
                                            VALID_TOKEN -> { // 토큰이 유효한 경우
                                                val intent = Intent(activity, MainActivity::class.java)
                                                activity.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                                                activity.finish()
                                            }
                                            FAIL_GET_TOKEN_AGAIN -> {
                                                Log.e(TAG, "요청 값 잘못됨 : 자동 로그인")
                                            }
                                            INVALID_TOKEN -> {
                                                Log.e(TAG, "유효하지 않은 유저 또는 토큰 : 자동 로그인")
                                            }
                                            SERVER_ERROR -> {
                                                Log.e(TAG, "서버 내부 오류 : 자동 로그인")
                                            }
                                            else -> { }
                                        }
                                    } else {
                                        Log.d(TAG, "${response.body()?.httpStatus}")
                                    }
                                }

                                override fun onFailure(call: Call<AgainToken>, t: Throwable) {
                                    Log.e(TAG, "자동 로그인 에러 : $t")
                                }
                            })
                        }
                } else if (tokenInfo == null) {
                    Log.d(TAG, "카카오 토큰 정보 존재하지 않음")
                    return@accessTokenInfo
                }
            }
        }
    }

    override fun login() {
        if (login_type != null && is_loggedIn_before)
            alreadyHaveAccount(activity)
        else {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(activity)) {
                UserApiClient.instance.loginWithKakaoTalk(activity, callback = callback)
            } else {
                UserApiClient.instance.loginWithKakaoAccount(activity, callback = callback)
            }
        }
    }

    override fun logout() {
        //사용자 액세스 토큰과 리프레시 토큰을 모두 만료시켜, 더 이상 해당 사용자 정보로 카카오 API를 호출할 수 없도록 합니다.
        // 로그아웃은 요청 성공 여부와 관계없이 토큰을 삭제 처리한다는 점에 유의합니다.
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Toast.makeText(activity, "로그아웃에 실패하였습니다 : $error", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "정상적으로 로그아웃되었습니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent(activity, LoginSelectActivity::class.java)
                // 스택 중간에 있었던 액티비티들을 지우는 역할
                activity.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                activity.finish()
            }
        }
    }

   // 카카오 플랫폼 안에서 앱과 사용자 카카오계정의 연결 상태를 해제합니다.
   // UserApiClient의 unlink()를 호출합니다.
  // 연결이 끊어지면 기존의 토큰은 더 이상 사용할 수 없으므로, 연결 끊기 요청 성공 시 로그아웃 처리가 함께 이뤄져 토큰이 삭제됩니다.
    override fun deleteAccount() {
        val alterDialog = AlertDialog.Builder(activity)
            .setMessage("회원 탈퇴 시 모든 데이터 복구가 불가능합니다.\n 정말 회원 탈퇴를 진행하시겠습니까?")
            .setPositiveButton("예") { dialog, which ->
                UserApiClient.instance.unlink { error ->
                    if (error != null) {
                        dialog?.dismiss()
                        Toast.makeText(
                            activity,
                            "회원 탈퇴 실패 $error",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        login_type = null
                        user_id = -1
                        access_token = null
                        refresh_token = null
                        is_loggedIn_before = false
                        dialog?.dismiss()
                        Toast.makeText(
                            activity,
                            "정상적으로 회원 탈퇴가 완료되었습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent =
                            Intent(activity, LoginSelectActivity::class.java)
                        // 스택 중간에 있었던 액티비티들을 지우는 역할
                        activity.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        activity.finish()
                    }
                }
            }
            .setNegativeButton("아니오"
            ) { dialog, which ->
                dialog?.run {
                    dismiss()
                }
            }
        alterDialog.show()
    }

    // 추후 유저 정보 셋팅 필요
    override fun getUserInfo() {
        UserApiClient.instance.me { user, error ->
            Toast.makeText(
                activity,
                "유저 정보 : 닉네임 -> ${user?.kakaoAccount?.profile?.nickname} , 이메일 -> ${user?.kakaoAccount?.email}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}