package com.reve.abroady.presentation.login.fragment

import android.content.Intent
import android.util.Log
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.reve.abroady.R
import com.reve.abroady.data.AuthState
import com.reve.abroady.databinding.FragmentLoginBinding
import com.reve.abroady.presentation.MainActivity
import com.reve.abroady.presentation.base.BaseFragment
import com.reve.abroady.presentation.login.activity.LoginActivity
import com.reve.abroady.util.Validation.isEmailValid
import com.reve.abroady.util.Validation.isPasswordValid

class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_login

    companion object {
        private const val GO_TO_FORGOT_PASSWORD = R.id.action_forgot_password
        private const val GO_TO_TERMS_OF_SERVICE = R.id.action_terms_of_service

        private const val NOT_CONNECTED = "A network error (such as timeout, interrupted connection or unreachable host) has occurred."
        private const val PASSWORD_NOT_SAME = "The password is invalid or the user does not have a password."
        private const val NOT_EXIST_EMAIL = "There is no user record corresponding to this identifier. The user may have been deleted."

        private const val CHECK_INTERNET_CONNECTION = "Please check your internet connection."
        private const val INVALID_EMAIL_OR_PASSWORD = "Please input valid email or password. Password should be 8 ~ 20 characters, including at least one number and one special character."
    }

    private val loginActivity by lazy { activity as? LoginActivity }

    override fun initStartView() {
        loginActivity?.fireBaseLoginViewModel?.isAlreadyLogin() // 로그인된 상태인지 확인
        initButtonListener()
        observeAuthState()
    }

    private fun observeAuthState() {
        loginActivity?.fireBaseLoginViewModel?.authState?.observe(this) { authState ->
            when (authState) {
                is AuthState.Success -> {
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    requireActivity().finish()
                }
                is AuthState.AuthError -> {
                    if (authState.message.equals(NOT_CONNECTED)) { // 인터넷 연결 안 됨
                        loginActivity?.showAlertDialog(getString(R.string.error),
                           CHECK_INTERNET_CONNECTION
                        )
                    }
                    else if (authState.message.equals(PASSWORD_NOT_SAME)) // 비밀번호 일치 X (혹은 이메일)
                        loginActivity?.showAlertDialog(getString(R.string.error), getString(R.string.login_error_message))
                    else if (authState.message.equals(NOT_EXIST_EMAIL)) { // 존재하지 않는 계정으로 로그인 시도
                        loginActivity?.showAlertDialog(getString(R.string.error), getString(R.string.not_exist_email))
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
                        binding.inputEmailLayout.background = ContextCompat.getDrawable(requireContext(), R.drawable.button_square_clicked)
                        binding.inputPasswordLayout.background = ContextCompat.getDrawable(requireContext(), R.drawable.button_square_default)
                    }
                    "Password" -> {
                        binding.inputPasswordLayout.background = ContextCompat.getDrawable(requireContext(), R.drawable.button_square_clicked)
                        binding.inputEmailLayout.background = ContextCompat.getDrawable(requireContext(), R.drawable.button_square_default)
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
                loginActivity?.fireBaseLoginViewModel?.login(email, password)
            else
                loginActivity?.showAlertDialog(getString(R.string.error),
                   INVALID_EMAIL_OR_PASSWORD
                )
        }
        binding.forgotPasswordText.setOnClickListener {
            this.findNavController().navigate(GO_TO_FORGOT_PASSWORD)
        }
        binding.signUpButton.setOnClickListener {
            this.findNavController().navigate(GO_TO_TERMS_OF_SERVICE)
        }
        binding.closeButton.setOnClickListener {
            requireActivity().finish()
        }
        setFocusChangeListener(binding.inputEmailText)
        setFocusChangeListener(binding.inputPasswordText)
    }
}