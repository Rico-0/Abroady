package com.reve.abroady.presentation.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.reve.abroady.R
import com.reve.abroady.databinding.ActivitySelectLoginBinding
import com.reve.abroady.presentation.login.classfile.LoginBase
import com.reve.abroady.util.ActivityList
import com.reve.abroady.util.LoginInstance
import com.reve.abroady.util.PreferenceManager.login_type

class LoginSelectActivity : AppCompatActivity() {

   private lateinit var binding : ActivitySelectLoginBinding

    private lateinit var loginInstance: LoginBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_login)
        ActivityList.actList.add(this)
        // 이미 로그인한 적이 있는 경우 자동로그인 (+ 토큰이 유효한지도 검사해야 함)
        login_type?.let {
           // loginInstance = LoginInstance.getLoginInstance(this, this@LoginSelectActivity, login_type!!)
           // loginInstance.checkAlreadyLoggedIn()
        }
        initButtonListener()
    }

    private fun initButtonListener() {
        binding.signInButton.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
        binding.signUpButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        binding.closeButton.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityList.actList.remove(this)
    }
}