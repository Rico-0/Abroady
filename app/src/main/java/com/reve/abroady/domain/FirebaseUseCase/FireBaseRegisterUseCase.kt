package com.reve.abroady.domain.FirebaseUseCase

import com.google.firebase.auth.AuthResult
import com.reve.abroady.data.repository.FireBaseAuthRepository
import com.reve.abroady.domain.Usecase
import io.reactivex.Observable

class FireBaseRegisterUseCase (val repository : FireBaseAuthRepository) : Usecase {
    fun register(email : String, password : String) : Observable<Any> {
        return repository.register(email, password)
    }
}