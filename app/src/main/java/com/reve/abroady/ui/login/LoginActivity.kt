package com.reve.abroady.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.reve.abroady.R
import com.reve.abroady.base.BaseActivity
import com.reve.abroady.databinding.ActivityCreateAccountBinding
import com.reve.abroady.databinding.ActivityLoginBinding
import com.reve.abroady.ui.MainActivity
import com.reve.abroady.ui.login.classfile.GoogleLogin
import com.reve.abroady.ui.login.classfile.KakaoLogin
import com.reve.abroady.ui.login.classfile.LoginBase
import com.reve.abroady.util.LoginInstance
import com.reve.abroady.util.PreferenceManager.login_type
import com.reve.abroady.viewmodel.MainViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var bindingLogin : ActivityLoginBinding
    private lateinit var bindingCreateAccount : ActivityCreateAccountBinding

    private lateinit var loginInstance: LoginBase
    private lateinit var loginType : String

   // private val mainViewModel : MainViewModel by lazy {
      //  ViewModelProvider(this).get(MainViewModel::class.java)
  //  }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingLogin = DataBindingUtil.setContentView(this, R.layout.activity_login)
        // 이미 로그인한 적이 있는 경우 자동로그인
        login_type?.let {
            loginInstance = LoginInstance.getLoginInstance(this, this@LoginActivity, login_type!!)
            loginInstance.checkAlreadyLoggedIn()
        }

        bindingLogin.kakaoLogin.setOnClickListener {
            initLoginInstance("kakao")
            loginInstance.login()
        }

        bindingLogin.googleLogin.setOnClickListener {
            initLoginInstance("google")
            (loginInstance as GoogleLogin).init()
            loginInstance.login()
        }
        bindingLogin.naverLogin.setOnClickListener {
            initLoginInstance("naver")
            loginInstance.login()
        }
    }

    private fun initLoginInstance(type : String) {
        loginType = type
        loginInstance = LoginInstance.getLoginInstance(this, this@LoginActivity, loginType)
    }

    // 구글 로그인 과정
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GoogleLogin.RC_SIGN_IN) {
            try {
                (loginInstance as GoogleLogin).setUserData(data)
                loginInstance.getUserInfo()
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("loginType", "google")
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)) // 스택에 있던 액티비티들을 지우는 역할
                finish()
            } catch (e : Exception){
                Log.d("LoginActivity", "구글 로그인으로 유저 정보 받아오는 중 에러 발생 : $e")
            }
        }
    }
}