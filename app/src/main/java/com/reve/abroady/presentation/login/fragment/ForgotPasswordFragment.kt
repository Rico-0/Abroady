package com.reve.abroady.presentation.login.fragment

import androidx.core.content.ContextCompat
import com.reve.abroady.R
import com.reve.abroady.databinding.FragmentForgotPasswordBinding
import com.reve.abroady.presentation.base.BaseFragment
import com.reve.abroady.presentation.login.activity.LoginActivity
import com.reve.abroady.util.OnBackPressedListener
import com.reve.abroady.util.Validation.isEmailValid

class ForgotPasswordFragment : BaseFragment<FragmentForgotPasswordBinding>(), OnBackPressedListener {
    override val layoutResourceId: Int
        get() = R.layout.fragment_forgot_password

    private val loginActivity by lazy { activity as? LoginActivity }

    override fun initStartView() {
        setButtonClickListener()
        setOnFocusChangeListener()
    }

    private fun setButtonClickListener() {
        binding.emailSendButton.setOnClickListener {
            val email = binding.inputEmailText.text.toString()
            if (!isEmailValid(email)) {
                loginActivity?.showAlertDialog(getString(R.string.error), getString(R.string.invalid_email) )
            } else {

            }
        }
        binding.backToLoginButton.setOnClickListener {
            loginActivity?.fireBaseLoginViewModel?.setAuthStateToIdle()
            onBackPressed()
        }
    }

    private fun showSelectDialog() {
        loginActivity?.setDialogLeftBtnClickListener {
            loginActivity?.selectDialog?.getDialog()?.dismiss()
        }
        loginActivity?.setDialogRightBtnClickListener {
            // TODO : 회원 가입 화면 이동
        }
        loginActivity?.showSelectDialog(getString(R.string.error), getString(R.string.sign_up))
    }

    private fun setOnFocusChangeListener() {
        binding.inputEmailText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.inputEmailLayout.background = ContextCompat.getDrawable(requireContext(), R.drawable.button_square_clicked)
                } else {
                binding.inputEmailLayout.background = ContextCompat.getDrawable(requireContext(), R.drawable.button_square_default)
            }
        }
    }

    override fun onBackPressed() {
        requireActivity().onBackPressed()
    }
}