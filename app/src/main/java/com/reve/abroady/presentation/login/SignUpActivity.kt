package com.reve.abroady.presentation.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.reve.abroady.R
import com.reve.abroady.databinding.ActivitySignupBinding
import com.reve.abroady.presentation.login.classfile.LoginBase
import com.reve.abroady.util.LoginInstance

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignupBinding

    private lateinit var loginInstance: LoginBase
    private lateinit var loginType : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup)
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
           // (loginInstance as GoogleLogin).init()
            loginInstance.login()
        }
        binding.naverLogin.setOnClickListener {
            initLoginInstance("naver")
            loginInstance.login()
        }
    }

    private fun initLoginInstance(type : String) {
        loginType = type
      //  loginInstance = LoginInstance.getLoginInstance(this, this@SignUpActivity, loginType)
    }

    // 구글 로그인 과정
  /*  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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
    } */
}