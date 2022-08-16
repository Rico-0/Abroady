package com.reve.abroady.presentation.login.classfile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.NidOAuthLoginState
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import com.reve.abroady.data.RetrofitInstance
import com.reve.abroady.data.entity.login.AgainToken
import com.reve.abroady.data.entity.login.SignUpUserData
import com.reve.abroady.data.entity.login.SignUpUserDataForSend
import com.reve.abroady.data.entity.login.SocialLogin
import com.reve.abroady.presentation.MainActivity
import com.reve.abroady.presentation.login.LoginSelectActivity
import com.reve.abroady.util.PreferenceManager.access_token
import com.reve.abroady.util.PreferenceManager.is_loggedIn_before
import com.reve.abroady.util.PreferenceManager.login_type
import com.reve.abroady.util.PreferenceManager.refresh_token
import com.reve.abroady.util.PreferenceManager.user_id
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/* class NaverLogin (
    private val context: Context,
    private val activity: Activity
) : LoginBase() {

    private val TAG: String = this.javaClass.simpleName

    companion object {
        private val loginDao = RetrofitInstance.getLoginDao()
        private const val NAVER = "naver"
        private const val SUCCESS_GET_TOKEN_AGAIN = 200
        private const val VALID_TOKEN = 202
        private const val FAIL_GET_TOKEN_AGAIN = 400
        private const val INVALID_TOKEN = 401
    }



    private val oauthLoginCallback = object : OAuthLoginCallback {
        override fun onSuccess() {
            // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
            // 로그인에 성공했을 때는 NaverIdLoginSDK.getAccessToken() 메서드로 접근 토큰 정보를 얻을 수 있습니다.
            /* binding.tvAccessToken.text = NaverIdLoginSDK.getAccessToken()
            binding.tvRefreshToken.text = NaverIdLoginSDK.getRefreshToken()
            binding.tvExpires.text = NaverIdLoginSDK.getExpiresAt().toString()
            binding.tvType.text = NaverIdLoginSDK.getTokenType()
            binding.tvState.text = NaverIdLoginSDK.getState().toString() */
            Log.d("NaverLogin", "네이버 로그인 성공! 접근 토근 : ${NaverIdLoginSDK.getAccessToken()}")
            if (!is_loggedIn_before && NaverIdLoginSDK.getAccessToken() != null) {
                signUp(NaverIdLoginSDK.getAccessToken().toString())
            } else if (is_loggedIn_before && NaverIdLoginSDK.getAccessToken() != null) {
                signIn(NaverIdLoginSDK.getAccessToken().toString())
            }
        }

        override fun onFailure(httpStatus: Int, message: String) {
            //로그인에 실패했다면 NaverIdLoginSDK.getLastErrorCode() 메서드나 NaverIdLoginSDK.getLastErrorDescription() 메서드로
            // 실패 이유와 에러 코드를 얻을 수 있습니다.
            val errorCode = NaverIdLoginSDK.getLastErrorCode().code
            val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
            Log.e(TAG, "errorCode:$errorCode, errorDesc:$errorDescription")
        }

        override fun onError(errorCode: Int, message: String) {
            onFailure(errorCode, message)
        }
    }

    override fun signUp(naverToken : String) {
        loginDao.socialLogin(NAVER, naverToken).enqueue(object :
            Callback<SocialLogin> {
            override fun onResponse(call: Call<SocialLogin>, response: Response<SocialLogin>) {
                if (response.isSuccessful) {
                    Log.d(TAG, "socialLogin status code : ${response.body()}")
                    val userData = response.body()?.userData
                    when (response.body()?.httpStatus) {
                        // DB에 회원 정보 한번 넣으면 소셜에서 탈퇴해도 삭제하는 게 없어서
                            // 자꾸 여기로 되어버림 ;; 임시 코드 (삭제 예정)
                        LOGIN_SUCCESS -> {
                            val userData = response.body()?.userData
                            userData?.run {
                                user_id = userData.id // 유저 식별 값
                                login_type = NAVER // 로그인 유형
                                access_token = userData.accessToken
                                refresh_token = userData.refreshToken
                                is_loggedIn_before = true // 로그인했는지 여부를 true로 변경
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
                                        "test user 2",
                                        "Korea",
                                        listOf("Korean")
                                    )
                                ).enqueue(object : Callback<SignUpUserData> {
                                    override fun onResponse(
                                        call: Call<SignUpUserData>,
                                        response: Response<SignUpUserData>
                                    ) {
                                        if (response.isSuccessful) {
                                            Log.d(TAG, "signup status code : ${response.body()}")
                                            when (response.body()?.httpStatus) {
                                                SIGNUP_SUCCESS -> { // 회원 가입 성공
                                                    // 서버에서 반환해 준 데이터를 SharedPreference에 저장
                                                    val userData = response.body()?.userData
                                                    userData?.run {
                                                        user_id = userData.id // 유저 식별 값
                                                        login_type = NAVER// 로그인 유형
                                                        access_token = userData.accessToken
                                                        refresh_token = userData.refreshToken
                                                        is_loggedIn_before = true // 로그인했는지 여부를 true로 변경
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

    override fun signIn(naverToken : String) {
        loginDao.socialLogin(NAVER, naverToken).enqueue(object :
            Callback<SocialLogin> {
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
        // 초기화가 필요하거나, 로그인이 필요하거나, 리프레시 토큰이 필요하지 않은 경우, 서버 내 토큰 유효성 여부 확인
        val state = NaverIdLoginSDK.getState()
        if (state == NidOAuthLoginState.OK) {
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
        } else if (state == NidOAuthLoginState.NEED_REFRESH_TOKEN) { // 액세스 토큰이 존재하지 않는 경우 (토큰 만료)
            // 리프레시 토큰이 존재하는 경우 이미 앱이 네이버와 연동되었다고 보고, 액세스 토큰을 갱신해 준다.
            NidOAuthLogin().callRefreshAccessTokenApi(context, oauthLoginCallback)
            val token = NaverIdLoginSDK.getAccessToken() // 갱신된 액세스 토큰 가져와서 로그인
            if (token != null) {
                loginDao.socialLogin(NAVER, token).enqueue(object : Callback<SocialLogin> {
                    override fun onResponse(
                        call: Call<SocialLogin>,
                        response: Response<SocialLogin>
                    ) {
                        if (response.isSuccessful) {
                            when (response.body()?.httpStatus) {
                                LOGIN_SUCCESS -> { // 로그인 성공 시
                                    val userData = response.body()?.userData
                                    userData?.run {
                                        user_id = userData.id // 유저 식별 값
                                        login_type = NAVER // 로그인 유형
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
        }
    }

    override fun login() {
        if (login_type != null && is_loggedIn_before)
            alreadyHaveAccount(activity)
        else {
            NaverIdLoginSDK.authenticate(context, oauthLoginCallback)
        }
    }

    override fun logout() {
        // NaverIdLoginSDK.logout() 메서드가 호출되면 클라이언트에 저장된 토큰이 삭제되고
        // NaverIdLoginSDK.getState() 메서드가 NidOAuthLoginState.NEED_LOGIN 값을 반환합니다.
        NaverIdLoginSDK.logout()
        val intent = Intent(activity, LoginSelectActivity::class.java)
        // 스택 중간에 있었던 액티비티들을 지우는 역할
        activity.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        activity.finish()
        Toast.makeText(activity, "정상적으로 로그아웃되었습니다.", Toast.LENGTH_SHORT).show()
    }

    override fun deleteAccount() {
        // 메서드로 연동을 해제할 때는 클라이언트에 저장된 토큰과 서버에 저장된 토큰을 모두 삭제합니다.
        // 이때 네트워크 오류가 발생하면 서버 호출에 실패하기 때문에 서버에 저장된 토큰을 삭제하지 못할 수 있습니다.
        // PC에서 네이버의 내정보 > 보안설정 > 외부 사이트 연결 페이지에 접속해 외부사이트 → 네이버에서 확인했을 때 연결 정보가 삭제되지 않은 채로 남아 있을 수 있습니다.
        NidOAuthLogin().callDeleteTokenApi(context, object : OAuthLoginCallback {
            override fun onError(errorCode: Int, message: String) {
                // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
                // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
                onFailure(errorCode, message)
            }

            override fun onFailure(httpStatus: Int, message: String) {
                // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
                // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
                Log.d("NaverLogin", "errorCode: ${NaverIdLoginSDK.getLastErrorCode().code}")
                Log.d("NaverLogin", "errorDesc: ${NaverIdLoginSDK.getLastErrorDescription()}")
            }

            override fun onSuccess() {
                login_type = null
                user_id = -1
                access_token = null
                refresh_token = null
                is_loggedIn_before = false
                val intent = Intent(activity, LoginSelectActivity::class.java)
                // 스택 중간에 있었던 액티비티들을 지우는 역할
                activity.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                activity.finish()
                Toast.makeText(activity, "정상적으로 회원 탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun getUserInfo() {
        NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse> {
            override fun onError(errorCode: Int, message: String) {
                Log.e("NaverLogin", "네이버 로그인으로 유저 정보 얻어오기 실패 : ${errorCode} , ${message}")
            }

            override fun onFailure(httpStatus: Int, message: String) {
                Log.e("NaverLogin", "네이버 로그인으로 유저 정보 얻어오기 실패 : ${httpStatus} , ${message}")
            }

            override fun onSuccess(result: NidProfileResponse) {
                Toast.makeText(
                    activity,
                    "유저 닉네임 : ${result.profile?.nickname} , 유저 이메일 : ${result.profile?.email} , 유저 프로필 사진 : ${result.profile?.profileImage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
} */