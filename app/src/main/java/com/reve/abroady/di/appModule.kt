package com.reve.abroady.di

import com.reve.abroady.data.dao.FireBaseLoginDao
import com.reve.abroady.data.repository.FireBaseAuthRepository
import com.reve.abroady.data.repository.FireBaseAuthRepositoryImpl
import com.reve.abroady.domain.FirebaseUseCase.FireBaseLoginUseCase
import com.reve.abroady.domain.FirebaseUseCase.FireBaseLogoutUseCase
import com.reve.abroady.domain.FirebaseUseCase.FireBaseRegisterUseCase
import com.reve.abroady.presentation.login.loginviewmodel.FireBaseLoginViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val appModule = module {
    //코루틴 디스패쳐
    single { Dispatchers.IO }

    //뷰모델
   single { FireBaseLoginViewModel(FireBaseLoginUseCase(FireBaseAuthRepositoryImpl(FireBaseLoginDao())),
            FireBaseRegisterUseCase(FireBaseAuthRepositoryImpl(FireBaseLoginDao())),
            FireBaseLogoutUseCase(FireBaseAuthRepositoryImpl(FireBaseLoginDao())))
          }

    //유스케이스
    single { FireBaseLoginUseCase(FireBaseAuthRepositoryImpl(FireBaseLoginDao()))}
    single { FireBaseLogoutUseCase(FireBaseAuthRepositoryImpl(FireBaseLoginDao())) }
    single { FireBaseRegisterUseCase(FireBaseAuthRepositoryImpl(FireBaseLoginDao())) }

    //레포지토리
    single { FireBaseAuthRepositoryImpl(FireBaseLoginDao()) }

    //Room DB


    //Retrofit


}