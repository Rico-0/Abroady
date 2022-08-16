package com.reve.abroady.data.repository

import com.google.firebase.auth.AuthResult
import io.reactivex.Observable
import com.reve.abroady.data.dao.FireBaseLoginDao

class FireBaseAuthRepositoryImpl(
    private val fireBaseLoginDao: FireBaseLoginDao
) : FireBaseAuthRepository {

    override fun login(email: String, password: String) : Observable<Any> {
        return fireBaseLoginDao.login(email, password)
    }

    override fun logout() {
        fireBaseLoginDao.logout()
    }

    override fun register(email: String, password: String) : Observable<Any> {
        return fireBaseLoginDao.register(email, password)
    }
}
