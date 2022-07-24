package com.reve.abroady.ui.login.classfile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import com.reve.abroady.ui.MainActivity
import com.reve.abroady.ui.login.LoginSelectActivity
import com.reve.abroady.util.PreferenceManager.login_type

class NaverLogin(
    private val context: Context,
    private val activity: Activity
) : LoginBase() {

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
            val intent = Intent(activity, MainActivity::class.java)
            intent.putExtra("loginType", "naver")
            activity.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            activity.finish()
        }

        override fun onFailure(httpStatus: Int, message: String) {
            //로그인에 실패했다면 NaverIdLoginSDK.getLastErrorCode() 메서드나 NaverIdLoginSDK.getLastErrorDescription() 메서드로
            // 실패 이유와 에러 코드를 얻을 수 있습니다.
            val errorCode = NaverIdLoginSDK.getLastErrorCode().code
            val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
            Toast.makeText(
                context,
                "errorCode:$errorCode, errorDesc:$errorDescription",
                Toast.LENGTH_SHORT
            ).show()
        }

        override fun onError(errorCode: Int, message: String) {
            onFailure(errorCode, message)
        }
    }

    override fun checkAlreadyLoggedIn() {
        // 관련 문서 없어서 일단 보류
    }

    override fun login() {
        NaverIdLoginSDK.authenticate(context, oauthLoginCallback)
    }

    override fun logout() {
        // NaverIdLoginSDK.logout() 메서드가 호출되면 클라이언트에 저장된 토큰이 삭제되고
        // NaverIdLoginSDK.getState() 메서드가 NidOAuthLoginState.NEED_LOGIN 값을 반환합니다.
        NaverIdLoginSDK.logout()
        login_type = null
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
}