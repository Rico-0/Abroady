package com.reve.abroady.ui.login.classfile

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.reve.abroady.ui.MainActivity
import com.reve.abroady.ui.login.LoginActivity
import com.reve.abroady.util.PreferenceManager.login_type

class GoogleLogin(private val activity: Activity) : LoginBase() {

    private lateinit var gso: GoogleSignInOptions
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var account: GoogleSignInAccount
    private lateinit var task: Task<GoogleSignInAccount>
    private lateinit var userData: Intent

    companion object {
        const val RC_SIGN_IN = 100
    }

    // TODO : gso, mGoogleSignInClient 객체 한 번만 생성해서 사용하는 방법 연구하기
    fun init() {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
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
        account?.let {
            val intent = Intent(activity, MainActivity::class.java)
            intent.putExtra("loginType", "google")
            activity.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            activity.startActivity(intent)
        }
    }

    override fun login() {
        val signInIntent = mGoogleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun logout() {
        init()
        mGoogleSignInClient?.let {
            mGoogleSignInClient.signOut()
                .addOnCompleteListener(activity) { task ->
                    task.addOnSuccessListener(activity) {
                        login_type = null
                        Toast.makeText(activity, "정상적으로 로그아웃되었습니다.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(activity, LoginActivity::class.java)
                        // 스택 중간에 있었던 액티비티들을 지우는 역할
                        activity.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        activity.finish()
                    }
                    task.addOnFailureListener(activity) { e ->
                        Toast.makeText(activity, "알 수 없는 이유로 로그아웃에 실패했습니다.", Toast.LENGTH_SHORT)
                            .show()
                        Log.e("GoogleLogin", "Logout error : $e")
                    }
                }
        }
    }

    override fun deleteAccount() {

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
}