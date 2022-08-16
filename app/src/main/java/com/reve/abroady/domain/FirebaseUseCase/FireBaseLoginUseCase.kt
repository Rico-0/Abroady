package com.reve.abroady.domain.FirebaseUseCase

import com.google.firebase.auth.AuthResult
import com.reve.abroady.data.repository.FireBaseAuthRepository
import com.reve.abroady.domain.Usecase
import io.reactivex.Observable

class FireBaseLoginUseCase (val repository : FireBaseAuthRepository) : Usecase {
    fun login(email : String, password : String) : Observable<Any> {
        return repository.login(email, password)
    }
}