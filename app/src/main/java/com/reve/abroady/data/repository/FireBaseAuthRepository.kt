package com.reve.abroady.data.repository

import io.reactivex.Observable
import com.google.firebase.auth.AuthResult

interface FireBaseAuthRepository {
    fun login(email : String, password : String) : Observable<Any>
    fun logout()
    fun register(email : String, password : String) : Observable<Any>
}