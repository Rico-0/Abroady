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
import com.reve.abroady.ui.MainActivity
import com.reve.abroady.ui.login.LoginSelectActivity
import com.reve.abroady.util.PreferenceManager.login_type

class KakaoLogin(private val activity: Activity) : LoginBase() {

    private val TAG: String = this.javaClass.simpleName
    private var loginType: String = "kakao"

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
            // 토큰값이 존재하면 login type 설정
            Log.d("KakaoLogin", "로그인 성공 : 액세스 토큰 값 -> ${token.accessToken}")
            val intent = Intent(activity, MainActivity::class.java)
            intent.putExtra("loginType", "kakao")
            activity.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)) // 스택에 있던 액티비티들을 지우는 역할
            activity.finish()
        }
    }

    override fun checkAlreadyLoggedIn() {
        if (AuthApiClient.instance.hasToken()) { // 사용자가 로그인 상태임을 보장하지 않으므로, 추후 백엔드와 연동하여 로그인 기능 구현 필요
            // 현재 로그인한 사용자의 엑세스 토큰 정보 보기
            // me 에서 제공되는 다양한 사용자 정보 없이 가볍게 토큰의 유효성을 체크하는 용도로 추천. 액세스 토큰이 만료된 경우 자동으로 갱신된 새로운 액세스 토큰 정보 반환
            UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                if (error != null) {
                    Log.d(TAG, "카카오 토큰 확인 중 에러 발생 : $error")
                    return@accessTokenInfo
                } else if (tokenInfo != null) { // 유효한 토큰 존재, 카카오 로그인 성공
                    val intent = Intent(activity, MainActivity::class.java)
                    activity.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    activity.finish()
                } else if (tokenInfo == null) {
                    Log.d(TAG, "카카오 토큰 정보 존재하지 않음")
                    return@accessTokenInfo
                }
            }
        }
    }

    override fun login() {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(activity)) {
            UserApiClient.instance.loginWithKakaoTalk(activity, callback = callback)
        } else {
            UserApiClient.instance.loginWithKakaoAccount(activity, callback = callback)
        }
    }

    override fun logout() {
        //사용자 액세스 토큰과 리프레시 토큰을 모두 만료시켜, 더 이상 해당 사용자 정보로 카카오 API를 호출할 수 없도록 합니다.
        // 로그아웃은 요청 성공 여부와 관계없이 토큰을 삭제 처리한다는 점에 유의합니다.
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Toast.makeText(activity, "로그아웃에 실패하였습니다 : $error", Toast.LENGTH_SHORT).show()
            } else {
                login_type = null
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
                        login_type = "null"
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
                dialog?.let {
                    it.dismiss()
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