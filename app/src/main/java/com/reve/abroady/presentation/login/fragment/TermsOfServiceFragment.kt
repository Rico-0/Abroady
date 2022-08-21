package com.reve.abroady.presentation.login.fragment

import androidx.core.content.ContextCompat
import com.reve.abroady.R
import com.reve.abroady.databinding.FragmentTermsOfServiceBinding
import com.reve.abroady.presentation.base.BaseFragment
import com.reve.abroady.presentation.login.loginviewmodel.TermsOfServiceViewModel
import com.reve.abroady.util.OnBackPressedListener
import org.koin.android.ext.android.inject

class TermsOfServiceFragment : BaseFragment<FragmentTermsOfServiceBinding>(), OnBackPressedListener {
    override val layoutResourceId: Int
        get() = R.layout.fragment_terms_of_service

    var isTermsConditionChecked = false
    var isCollectUsePersonalChecked = false
    var isAllAcceptChecked = false

    private val termsOfServiceViewModel : TermsOfServiceViewModel by inject()

    override fun initStartView() {
        initButtonListener()
        observeData()
    }

    private fun initButtonListener() {
        binding.termsConditionsButton.setOnClickListener {
            if (isTermsConditionChecked) termsOfServiceViewModel.uncheckTermsCondition() else termsOfServiceViewModel.checkTermsCondition()
        }
        binding.collectUsePersonalButton.setOnClickListener {
            if (isCollectUsePersonalChecked) termsOfServiceViewModel.uncheckCollectUsePersonal() else termsOfServiceViewModel.checkCollectUsePersonal()
        }
        binding.acceptAllButton.setOnClickListener {
            if (isAllAcceptChecked) termsOfServiceViewModel.uncheckAcceptAll() else termsOfServiceViewModel.checkAcceptAll()
        }
        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun observeData() {
        termsOfServiceViewModel.termsCondition.observe(viewLifecycleOwner) {
            isTermsConditionChecked = it
            if (isTermsConditionChecked) {
                binding.termsConditionsButton.setImageResource(R.drawable.button_terms_of_services_radio_circle_colored)
            } else {
                binding.termsConditionsButton.setImageResource(R.drawable.button_terms_of_services_radio_circle_default)
            }
        }

        termsOfServiceViewModel.collectUsePersonal.observe(viewLifecycleOwner) {
            isCollectUsePersonalChecked = it
            if (isCollectUsePersonalChecked) {
                binding.collectUsePersonalButton.setImageResource(R.drawable.button_terms_of_services_radio_circle_colored)
            } else {
                binding.termsConditionsButton.setImageResource(R.drawable.button_terms_of_services_radio_circle_default)
            }
        }

        termsOfServiceViewModel.acceptAll.observe(viewLifecycleOwner) {
            isAllAcceptChecked = it
            if (isAllAcceptChecked) { // 전부 동의하기 체크 시 다른 항목들도 체크됨
                binding.acceptAllButton.setImageResource(R.drawable.button_terms_of_services_radio_circle_colored)
                setContinueAvailable()
            } else { // 전부 동의하기 체크 해제 시 다른 항목들도 체크 해제됨
                binding.acceptAllButton.setImageResource(R.drawable.button_terms_of_services_radio_circle_default)
                setContinueUnavailable()
            }
        }
    }

    private fun setContinueAvailable() {
        binding.continueButton.background = ContextCompat.getDrawable(requireContext(), R.drawable.button_terms_of_services_continue_colored)
    }

    private fun setContinueUnavailable() {
        binding.continueButton.background = ContextCompat.getDrawable(requireContext(), R.drawable.button_terms_of_services_continue_light)
    }

    override fun onBackPressed() {
        requireActivity().onBackPressed()
    }
}