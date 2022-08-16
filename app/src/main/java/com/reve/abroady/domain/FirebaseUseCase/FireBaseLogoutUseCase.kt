package com.reve.abroady.domain.FirebaseUseCase

import com.reve.abroady.data.repository.FireBaseAuthRepository
import com.reve.abroady.domain.Usecase

class FireBaseLogoutUseCase (val repository : FireBaseAuthRepository) : Usecase {
   fun logout() {
        return repository.logout()
    }
}