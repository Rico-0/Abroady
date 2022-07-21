package com.reve.abroady.ui.login.classfile

abstract class LoginBase {
    abstract fun checkAlreadyLoggedIn()
    abstract fun login()
    abstract fun logout()
    abstract fun deleteAccount()
    abstract fun getUserInfo()
}