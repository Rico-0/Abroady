package com.reve.abroady.presentation.login.loginviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.reve.abroady.presentation.base.BaseViewModel

class TermsOfServiceViewModel : BaseViewModel() {

    private val _termsCondition = MutableLiveData<Boolean>()
        val termsCondition : LiveData<Boolean> get() = _termsCondition

    private val _collectUsePersonal = MutableLiveData<Boolean>()
        val collectUsePersonal : LiveData<Boolean> get() = _collectUsePersonal

    // 약관 동의에 전부 체크했는지 상태를 나타내는 LiveData
    private val _acceptAll = MutableLiveData<Boolean>()
        val acceptAll: LiveData<Boolean> = _acceptAll

    fun checkTermsCondition() {
        _termsCondition.value = true
    }

    fun uncheckTermsCondition() {
        _termsCondition.value = false
    }

    fun checkCollectUsePersonal() {
        _collectUsePersonal.value = true
    }

    fun uncheckCollectUsePersonal() {
        _collectUsePersonal.value = false
    }

    fun checkAcceptAll() {
        _termsCondition.value = true
        _collectUsePersonal.value = true
        _acceptAll.value = true
    }

    fun uncheckAcceptAll() {
        _termsCondition.value = false
        _collectUsePersonal.value = false
        _acceptAll.value = false
    }
}