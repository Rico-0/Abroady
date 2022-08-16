package com.reve.abroady.presentation.login.classfile

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.Scope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.reve.abroady.R
import com.reve.abroady.presentation.MainActivity

class FirebaseLogin(private val activity : Activity) {

    private val TAG = this.javaClass.simpleName

    val FIREBASE_LOGIN_CODE = 1

    private lateinit var gso: GoogleSignInOptions
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    companion object {
        private val auth = FirebaseAuth.getInstance()
       // private val actionCodeSettings = ActionCodeSettings.newBuilder()
    }

    fun init() {
        val clientId = activity.getString(R.string.google_client_id)
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(clientId)
            .requestScopes(Scope(Scopes.DRIVE_APPFOLDER))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso)
    }

   fun signUp(email : String, password : String) {
        auth?.createUserWithEmailAndPassword(email, password)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) { // 정상적으로 이메일과 비밀번호가 전달되어 새 유저 계정을 생성 + 서버 DB에 저장 완료 + 로그인 됨
                    goMainActivity()
                }
                else if (!task.exception?.message.isNullOrEmpty()) { // 예외 메시지가 있다면 출력 (서버 연결 실패 등)
                    Log.e(TAG, "파이어베이스 회원 가입 중 오류 발생 : ${task.exception?.message}")
                } else { // 이미 DB에 해당 이메일과 패스워드가 있는 경우
                    signIn(email, password)
                }
            }
    }

    fun signIn(email : String, password : String) {
        auth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener {  //통신 완료가 된 후 무슨일을 할지
                    task ->
                if(task.isSuccessful){
                    goMainActivity()
                }
                else{
                    // 오류가 난 경우!
                    Log.e(TAG, "파이어베이스 로그인 중 오류 발생 : ${task.exception?.message}")
                }
            }
    }

    private fun goMainActivity() {
        val intent = Intent(activity, MainActivity::class.java)
        activity.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)) // 스택에 있던 액티비티들을 지우는 역할
        activity.finish()
    }

    fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        //구글로부터 로그인된 사용자의 정보(Credentail)을 얻어온다.
        val credential = GoogleAuthProvider.getCredential(account?.idToken!!, null)
        //그 정보를 사용하여 Firebase의 auth를 실행한다.
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener {  // 통신 완료가 된 후 무슨일을 할지
                    task ->
                if (task.isSuccessful) {
                    // 로그인 처리를 해주면 됨!
                    goMainActivity()
                } else {
                    // 오류가 난 경우!
                    Log.e(TAG, "파이어베이스 서버에 사용자 정보 보내는 중 중 오류 발생 : ${task.exception?.message}")
                }
            }
    }

   /* private fun setActionCodeSettings() = run {
        actionCodeSettings?.run {
            url = "https://www.abroady-reve.userauth.com"
            handleCodeInApp = true
            setAndroidPackageName("com.reve.abroady", true, "24")
        }
    }

    fun sendSignInLinkToEmail(email : String) = run {
        setActionCodeSettings()
        auth?.run {
            sendSignInLinkToEmail(email, actionCodeSettings.build())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(activity, "Auth Email was sent.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "이메일 인증 링크 보내기 실패 : $e")
            }
        }
    } */
}