package com.reve.abroady.ui.login.classfile

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.reve.abroady.R
import com.reve.abroady.model.data.RetrofitInstance
import com.reve.abroady.model.data.login.AgainToken
import com.reve.abroady.model.data.login.SignUpUserData
import com.reve.abroady.model.data.login.SignUpUserDataForSend
import com.reve.abroady.model.data.login.SocialLogin
import com.reve.abroady.ui.MainActivity
import com.reve.abroady.ui.login.LoginSelectActivity
import com.reve.abroady.util.PreferenceManager
import com.reve.abroady.util.PreferenceManager.access_token
import com.reve.abroady.util.PreferenceManager.is_loggedIn_before
import com.reve.abroady.util.PreferenceManager.login_type
import com.reve.abroady.util.PreferenceManager.refresh_token
import com.reve.abroady.util.PreferenceManager.user_id
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GoogleLogin(private val activity: Activity) : LoginBase() {

    private val TAG = this.javaClass.simpleName

    private lateinit var gso: GoogleSignInOptions
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var account: GoogleSignInAccount
    private lateinit var task: Task<GoogleSignInAccount>
    private lateinit var userData: Intent

    companion object {
        private val loginDao = RetrofitInstance.getLoginDao()
        const val RC_SIGN_IN = 100
        private const val GOOGLE = "google"
        private const val SUCCESS_GET_TOKEN_AGAIN = 200
        private const val VALID_TOKEN = 202
        private const val FAIL_GET_TOKEN_AGAIN = 400
        private const val INVALID_TOKEN = 401
    }

    fun init() {
        val serverClientId = activity.getString(R.string.naver_client_id)
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope(Scopes.DRIVE_APPFOLDER))
            .requestServerAuthCode(serverClientId)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso)
    }

    fun setUserData(data: Intent?) {
        if (data != null) {
            userData = data
        }
    }

    override fun checkAlreadyLoggedIn() {
        // GoogleSignInAccount 객체가 null이 아니라면 이전에 google로 앱에 로그인한 것임
        val account: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(activity)
        account?.run {
            loginDao.getTokenAgain(PreferenceManager.access_token!!, PreferenceManager.refresh_token!!).enqueue(object :
                Callback<AgainToken> {
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
    }

    override fun login() {
        val signInIntent = mGoogleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun logout() {
        init()
        mGoogleSignInClient?.run {
            mGoogleSignInClient.signOut()
                .addOnCompleteListener(activity) { task ->
                    task.addOnSuccessListener(activity) {
                        Toast.makeText(activity, "정상적으로 로그아웃되었습니다.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(activity, LoginSelectActivity::class.java)
                        // 스택 중간에 있었던 액티비티들을 지우는 역할
                        activity.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        activity.finish()
                    }
                    task.addOnFailureListener(activity) { e ->
                        Toast.makeText(activity, "알 수 없는 이유로 로그아웃에 실패했습니다.", Toast.LENGTH_SHORT)
                            .show()
                        Log.e(TAG, "Logout error : $e")
                    }
                }
        }
    }

    override fun deleteAccount() {
        init()
        mGoogleSignInClient?.run {
            mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener { task ->
                    task.addOnSuccessListener(activity) {
                        Toast.makeText(activity, "정상적으로 회원 탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(activity, LoginSelectActivity::class.java)
                        // 스택 중간에 있었던 액티비티들을 지우는 역할
                        activity.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        activity.finish()
                    }
                    task.addOnFailureListener(activity) { e ->
                        Toast.makeText(activity, "알 수 없는 이유로 회원 탈퇴에 실패했습니다.", Toast.LENGTH_SHORT)
                            .show()
                        Log.e(TAG, "failed to delete account : $e")
                    }
                }
        }
    }

    override fun getUserInfo() {
        task = GoogleSignIn.getSignedInAccountFromIntent(userData)
        account = task.getResult(ApiException::class.java)
        // 추후 수정
        Toast.makeText(
            activity,
            "DisplayName : ${account.displayName} , email : ${account.email} , id : ${account.id} , idToken : ${account.idToken} , profileUri : ${account.photoUrl}",
            Toast.LENGTH_LONG
        ).show()
    }

    // 아직 기능 미구현 상태
    fun getAccessToken(authCode : String) : String {
        task = GoogleSignIn.getSignedInAccountFromIntent(userData)
        try {
            val account = task.getResult(ApiException::class.java)
            val authCode = account.serverAuthCode
        } catch (e : ApiException) {
            Log.e(TAG, "구글 액세스 토큰 가져오는 중 에러 발생 : $e")
        }
        return ""
    }

    override fun signUp(googleToken : String) {
        loginDao.socialLogin(GOOGLE, googleToken).enqueue(object : Callback<SocialLogin> {
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
                                login_type = GOOGLE // 로그인 유형
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
                                        "test user 3",
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
                                                        login_type = GOOGLE // 로그인 유형
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

    override fun signIn(googleToken: String) {
        loginDao.socialLogin(GOOGLE, googleToken).enqueue(object : Callback<SocialLogin> {
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
}