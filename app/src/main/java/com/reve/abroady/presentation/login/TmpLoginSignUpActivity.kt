package com.reve.abroady.presentation.login

import android.app.AlertDialog
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import com.reve.abroady.R
import com.reve.abroady.data.AuthState
import com.reve.abroady.databinding.TmpLoginActivityBinding
import com.reve.abroady.presentation.MainActivity
import com.reve.abroady.presentation.base.BaseActivity
import com.reve.abroady.presentation.login.loginviewmodel.FireBaseLoginViewModel
import com.reve.abroady.util.Validation.isEmailValid
import com.reve.abroady.util.Validation.isPasswordValid
import com.reve.abroady.util.Validation.isSamePassword
import org.koin.android.ext.android.inject

class TmpLoginSignUpActivity : BaseActivity<TmpLoginActivityBinding>() {

    private val fireBaseLoginViewModel : FireBaseLoginViewModel by inject()

    override val layoutResourceId: Int
        get() = R.layout.tmp_login_activity

    // TODO : 로그인 / 회원가입 분기 처리 필요
    override fun initStartView() {
        addValidationCheckListener(binding.inputEmail)
        addValidationCheckListener(binding.inputPassword)
        binding.signupButton.setOnClickListener {
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPassword.text.toString()
            val confirmPassword = binding.confirmPassword.text.toString()
            if (isEmailValid(email) && isPasswordValid(password) && isSamePassword(password, confirmPassword))
                fireBaseLoginViewModel.register(email, password)
            else
                showValidValueDialog()
        }
        binding.loginButton.setOnClickListener {
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPassword.text.toString()
            if (isEmailValid(email) && isPasswordValid(password))
                fireBaseLoginViewModel.login(email, password)
            else
                showValidValueDialog()
        }
        fireBaseLoginViewModel.isAlreadyLogin() // 이미 로그인한 유저가 존재할 경우 MainActivity로 넘어가도록 함
        observeAuthState()
    }

    private fun showValidValueDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Invalid email or password")
            .setMessage("Please input valid email or password.\nPassword should be 8 ~ 20 characters, including at least one number and one special character.")
            .setPositiveButton("I see") { dialog, _ ->
                dialog?.dismiss()
            }
        dialog.show()
    }

    private fun observeAuthState() {
        fireBaseLoginViewModel.authState.observe(this, { authState ->
            if (authState == AuthState.Success) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            } else if (authState == AuthState.AuthError("The email address is already in use by another account.")) {
                Toast.makeText(
                    this,
                    "The email address is already in use by another account.",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (authState == AuthState.AuthError()) {
                Toast.makeText(
                    this,
                    "Unknown error occured when logging in. Check your Internet connection or try later.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun addValidationCheckListener(editText : EditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
            // 이메일, 비밀번호 입력의 변경 사항을 실시간으로 알려줌
            override fun afterTextChanged(s: Editable?) {
                val emailState = isEmailValid(binding.inputEmail.text.toString())
                if (!emailState) {
                    //binding.inputEmailState.text = "not valid email"
                }
                val passwordState = isPasswordValid(binding.inputPassword.text.toString())
                if (!passwordState) {
                    //binding.inputPasswordState.text = "not valid password"
                }
                val confirmState = isSamePassword(binding.inputPassword.text.toString(), binding.confirmPassword.text.toString())
                if (!confirmState) {
                    //binding.confirmState.text = "password isn't same"
                }
            }
        })
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