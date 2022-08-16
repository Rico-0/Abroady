package com.reve.abroady.data

// 사용자 인증 상태 (State 패턴)
sealed class AuthState {
    object Idle : AuthState() // 로그인이 되지 않은 상태
    object Loading : AuthState()
    object Success : AuthState() // 로그인, 회원가입에 성공한 상태
    class AuthError(val message: String? = null) : AuthState()
}
