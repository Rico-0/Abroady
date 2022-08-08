package com.reve.abroady.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.reve.abroady.R
import com.reve.abroady.databinding.ActivitySigninBinding
import com.reve.abroady.ui.MainActivity
import com.reve.abroady.ui.login.classfile.GoogleLogin
import com.reve.abroady.ui.login.classfile.LoginBase
import com.reve.abroady.util.LoginInstance
import com.reve.abroady.util.PreferenceManager

class SignInActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySigninBinding

    private lateinit var loginInstance: LoginBase
    private lateinit var loginType : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signin)
        initButtonListener()
    }

    private fun initButtonListener() {
        binding.buttonGoBack.setOnClickListener {
            finish()
        }
        binding.kakaoLogin.setOnClickListener {
            initLoginInstance("kakao")
            loginInstance.login()
        }

        binding.googleLogin.setOnClickListener {
            initLoginInstance("google")
            (loginInstance as GoogleLogin).init()
            loginInstance.login()
        }
        binding.naverLogin.setOnClickListener {
            initLoginInstance("naver")
            loginInstance.login()
        }
    }

    private fun initLoginInstance(type : String) {
        loginType = type
        loginInstance = LoginInstance.getLoginInstance(this, this@SignInActivity, loginType)
    }

    // 구글 로그인 과정
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GoogleLogin.RC_SIGN_IN) {
            try {
                (loginInstance as GoogleLogin).setUserData(data)
                loginInstance.getUserInfo()
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)

            } catch (e : Exception){
                Log.d("LoginActivity", "구글 로그인으로 유저 정보 받아오는 중 에러 발생 : $e")
            }
        }
    }
}