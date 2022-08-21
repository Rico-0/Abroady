package com.reve.abroady.presentation.login

import android.content.Intent
import android.util.Log
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.reve.abroady.R
import com.reve.abroady.data.AuthState
import com.reve.abroady.presentation.MainActivity
import com.reve.abroady.presentation.base.BaseActivity
import com.reve.abroady.presentation.login.fragment.ForgotPasswordFragment
import com.reve.abroady.presentation.login.loginviewmodel.FireBaseLoginViewModel
import com.reve.abroady.util.ActivityList
import com.reve.abroady.util.DialogAlert
import com.reve.abroady.util.Validation.isEmailValid
import com.reve.abroady.util.Validation.isPasswordValid
import org.koin.android.ext.android.inject

/* class LoginSelectActivity : BaseActivity<ActivitySelectLoginBinding>() {

    override val layoutResourceId: Int
        get() = R.layout.activity_select_login

    // private lateinit var loginInstance: LoginBase

    companion object {
        private const val NOT_CONNECTED = "A network error (such as timeout, interrupted connection or unreachable host) has occurred."
        private const val PASSWORD_NOT_SAME = "The password is invalid or the user does not have a password."
        private const val NOT_EXIST_EMAIL = "There is no user record corresponding to this identifier. The user may have been deleted."

        private const val INVALID_EMAIL_OR_PASSWORD = "Please input valid email or password. Password should be 8 ~ 20 characters, including at least one number and one special character."
        private const val CHECK_INTERNET_CONNECTION = "Please check your internet connection."
    }

    private val fireBaseLoginViewModel : FireBaseLoginViewModel by inject()

    private val forgotPasswordFragment : ForgotPasswordFragment by lazy {
        ForgotPasswordFragment()
    }

    override fun initStartView() {
        ActivityList.actList.add(this)
        /* 이미 로그인한 적이 있는 경우 자동로그인 (+ 토큰이 유효한지도 검사해야 함)
        login_type?.let {
             loginInstance = LoginInstance.getLoginInstance(this, this@LoginSelectActivity, login_type!!)
             loginInstance.checkAlreadyLoggedIn()
        } */

        initButtonListener()
        fireBaseLoginViewModel.isAlreadyLogin() // 이미 로그인한 유저가 존재할 경우 MainActivity로 넘어가도록 함
        observeAuthState()
    }

    private fun observeAuthState() {
        fireBaseLoginViewModel.authState.observe(this) { authState ->
            when (authState) {
                is AuthState.Success -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    finish()
                }
                is AuthState.AuthError -> {
                    if (authState.message.equals(NOT_CONNECTED)) { // 인터넷 연결 안 됨
                        showValidValueDialog(getString(R.string.error), CHECK_INTERNET_CONNECTION)
                    }
                    else if (authState.message.equals(PASSWORD_NOT_SAME)) // 비밀번호 일치 X (혹은 이메일)
                        showValidValueDialog(getString(R.string.error), getString(R.string.invalid_password))
                    else if (authState.message.equals(NOT_EXIST_EMAIL)) { // 존재하지 않는 계정으로 로그인 시도
                        showValidValueDialog(getString(R.string.error), getString(R.string.not_exist_email))
                    } else {
                        Log.e(TAG, "login error : ${authState.message}")
                    }
                }
            }
        }
    }

    private fun setFocusChangeListener(editText: EditText) {
        editText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                when (editText.hint) {
                    "Email" -> {
                        binding.inputEmailLayout.background = ContextCompat.getDrawable(this, R.drawable.button_square_clicked)
                        binding.inputPasswordLayout.background = ContextCompat.getDrawable(this, R.drawable.button_square_default)
                    }
                    "Password" -> {
                        binding.inputPasswordLayout.background = ContextCompat.getDrawable(this, R.drawable.button_square_clicked)
                        binding.inputEmailLayout.background = ContextCompat.getDrawable(this, R.drawable.button_square_default)
                    }
                }
            }
        }
    }

    private fun initButtonListener() {
        binding.signInButton.setOnClickListener {
            val email = binding.inputEmailText.text.toString()
            val password = binding.inputPasswordText.text.toString()
            if (isEmailValid(email) && isPasswordValid(password))
                fireBaseLoginViewModel.login(email, password)
            else
                showValidValueDialog(getString(R.string.error), INVALID_EMAIL_OR_PASSWORD)
        }
        binding.forgotPasswordText.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.activity_select_login_layout, forgotPasswordFragment)
                .setReorderingAllowed(true) // 불필요한 트랜잭션 최소화하여 프래그먼트 상태 전환 최적화
                .addToBackStack(null) // 뒤로가기 눌렀을 때 이전 화면으로 돌아갈 수 있도록 함
                .commit()
        }
        binding.closeButton.setOnClickListener {
            finish()
        }
        setFocusChangeListener(binding.inputEmailText)
        setFocusChangeListener(binding.inputPasswordText)
    }

    private fun showValidValueDialog(title : String, content : String) {
        val dialog = DialogAlert(this)
        dialog.start(title, content)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityList.actList.remove(this)
    }
} */