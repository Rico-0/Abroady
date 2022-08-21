package com.reve.abroady.presentation.login.loginviewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.reve.abroady.data.AuthState
import com.reve.abroady.presentation.base.BaseViewModel

class FireBaseLoginViewModel : BaseViewModel() {

    // 인증 상태를 나타내는 LiveData 선언
    private val _authState by lazy { MutableLiveData<AuthState>(AuthState.Idle) }
    val authState: LiveData<AuthState> = _authState

    // 인증 상태가 Error인 경우, 다른 프래그먼트에서 로그인 화면으로 돌아갈 시 error 메시지 출력되는 경우 방지
    fun setAuthStateToIdle() {
        _authState.value = AuthState.Idle
    }

    fun isAlreadyLogin() {
        FirebaseAuth.getInstance().currentUser?.let {
            _authState.value = AuthState.Success
        }
    }

    fun register(email: String, password: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i(TAG, "Email signup is successful")
                    _authState.value = AuthState.Success
                } else {
                    task.exception?.let {
                        Log.i(TAG, "Email signup failed with error ${it.localizedMessage}")
                        _authState.value = AuthState.AuthError(it.localizedMessage)
                    }
                }
            }
    }

    fun login(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i(TAG, "Email signin is successful")
                    _authState.value = AuthState.Success
                }
            }.addOnFailureListener { exception ->
                Log.i(TAG, "Email signin failed with error ${exception.localizedMessage}")
                _authState.value = AuthState.AuthError(exception.localizedMessage)
            }
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
        _authState.value = AuthState.Idle
    }
}