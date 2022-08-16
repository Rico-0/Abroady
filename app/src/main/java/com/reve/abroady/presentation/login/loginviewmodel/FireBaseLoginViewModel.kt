package com.reve.abroady.presentation.login.loginviewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.reve.abroady.domain.FirebaseUseCase.FireBaseLoginUseCase
import com.reve.abroady.domain.FirebaseUseCase.FireBaseLogoutUseCase
import com.reve.abroady.domain.FirebaseUseCase.FireBaseRegisterUseCase
import com.reve.abroady.presentation.base.BaseViewModel
import io.reactivex.schedulers.Schedulers

class FireBaseLoginViewModel(
    private val loginUseCase: FireBaseLoginUseCase,
    private val registerUseCase: FireBaseRegisterUseCase,
    private val logoutUseCase: FireBaseLogoutUseCase
) : BaseViewModel() {

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    fun login(email: String, password: String) {
        addDisposable(
            loginUseCase.login(email, password)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it is AuthResult) { // 만약 파이어베이스 로그인이 성공해서
                        val email = it.user?.email
                        if (!email.isNullOrEmpty())
                            _userName.postValue(
                                email?.substring(
                                    0,
                                    email?.indexOf("@")
                                )
                            ) // 임시로 유저 이름을 이메일 아이디로 설정
                        else // String (error 발생 시)
                            Log.e(TAG, "login error in subscribe() : ${it}")
                    }
                }, {
                    errorSubject.onNext(it)
                    Log.e(TAG, "viewmodel login error : $it")
                })
        )
    }


    fun register(email: String, password: String) {
        addDisposable(
            registerUseCase.register(email, password)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it is AuthResult) { // 만약 파이어베이스 회원가입이 성공한 경우
                        val email = it.user?.email // 인정 결과에서 유저 이메일을 받아와서
                        if (!email.isNullOrEmpty())
                            _userName.postValue(email?.substring(0, email?.indexOf("@"))) // 임시로 유저 이름을 @ 앞의 문자로 설정
                        else // String 값이 넘어온 경우 (error 발생 시)
                            Log.e(TAG, "login error in subscribe() : ${it}")
                    }
                }, {
                    errorSubject.onNext(it)
                    Log.e(TAG, "viewmodel login error : $it")
                })
        )
    }

    fun logout() {
        logoutUseCase.logout()
    }
}