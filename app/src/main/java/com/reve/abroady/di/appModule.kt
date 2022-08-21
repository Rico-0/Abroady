package com.reve.abroady.di

import com.reve.abroady.presentation.login.loginviewmodel.FireBaseLoginViewModel
import com.reve.abroady.presentation.login.loginviewmodel.TermsOfServiceViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

internal val appModule = module {
    //코루틴 디스패쳐
    single { Dispatchers.IO }

    //뷰모델
    single { FireBaseLoginViewModel() }
    single { TermsOfServiceViewModel() }

    //유스케이스

    //레포지토리

    //Room DB

    //Retrofit


}