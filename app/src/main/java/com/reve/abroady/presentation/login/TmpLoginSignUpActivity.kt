package com.reve.abroady.presentation.login

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import com.reve.abroady.R
import com.reve.abroady.presentation.base.BaseActivity
import com.reve.abroady.databinding.TmpLoginActivityBinding
import com.reve.abroady.presentation.MainActivity
import com.reve.abroady.presentation.login.loginviewmodel.FireBaseLoginViewModel
import org.koin.android.ext.android.inject
import java.util.regex.Pattern

class TmpLoginSignUpActivity : BaseActivity<TmpLoginActivityBinding>() {

    private val fireBaseLoginViewModel : FireBaseLoginViewModel by inject()

    override val layoutResourceId: Int
        get() = R.layout.tmp_login_activity

    // TODO : 로그인 / 회원가입 분기 처리 필요
    override fun initStartView() {
        addValidationCheckListener(binding.inputEmail)
        addValidationCheckListener(binding.inputPassword)
        binding.signUpLoginButton.setOnClickListener {
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPassword.text.toString()
            val inputState = checkEmailAndPassword(email, password)
            if (inputState == "OK") {
                fireBaseLoginViewModel.register(email, password)
            }
            else
                Toast.makeText(this, inputState, Toast.LENGTH_SHORT).show()
        }
        observeUserData()
    }

    private fun observeUserData() {
        fireBaseLoginViewModel.userName.observe(this, { userName ->
            if (!userName.isNullOrEmpty()) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            } else {
                Toast.makeText(this, "Unknown error occured when logging in. Check your Internet connection or try later.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addValidationCheckListener(editText : EditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
            // 이메일, 비밀번호 입력의 변경 사항을 실시간으로 알려줌
            override fun afterTextChanged(s: Editable?) {
                val inputState = checkEmailAndPassword(binding.inputEmail.text.toString(), binding.inputPassword.text.toString())
                // binding.inputEmailState.text = inputState
                // binding.inputPasswordState.text = inputState
            }
        })
    }

    // 이메일, 비밀번호 유효성 검사
    private fun checkEmailAndPassword(email : String, password : String) : String {
        val emailPattern = Patterns.EMAIL_ADDRESS
        val emailMatcher = emailPattern.matcher(email)
        val pwRegex =  "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&.])[A-Za-z[0-9]$@$!%*#?&.]{8,20}$" // 8 ~ 20자리 사이의 비밀번호. 영문, 숫자, 특수문자 포함
        if (email.isNullOrEmpty() || !(emailMatcher.find())) {
            return "Please check your email address."
        } else if (password.isNullOrEmpty() || !(Pattern.matches(pwRegex, password))) {
            return "Please check your password. (At least 8 characters, include 1 number and 1 special character)"
        } else
            return "OK"
    }

    /* 구글 로그인 과정
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == fireBaseLogin.FIREBASE_LOGIN_CODE && resultCode == Activity.RESULT_OK) {
           val result = data?.let { Auth.GoogleSignInApi.getSignInResultFromIntent(it) }
            if (result?.isSuccess == true) {
                val account = result.signInAccount
                fireBaseLogin.firebaseAuthWithGoogle(account)
            } else {
                Log.e(TAG, "${result?.status}")
            }
        }
    } */
}