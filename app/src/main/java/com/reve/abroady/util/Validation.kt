package com.reve.abroady.util

import android.util.Patterns
import java.util.regex.Pattern

object Validation {

    const val EMAIL_NOT_VALID = "Please check your email address."
    const val PASSWORD_NOT_VALID = "Please set your password at least 8 characters, 1 number, 1 special characters."
    const val PASSWORD_CONFIRM_NOT_SAME = "Please input your password and confirm password same."

    fun isEmailValid(email : String) : Boolean {
        val emailPattern = Patterns.EMAIL_ADDRESS
        val emailMatcher = emailPattern.matcher(email)
        return !email.isNullOrEmpty() && emailMatcher.find()
    }

    // 비밀번호 유효성 검사
    fun isPasswordValid(password : String) : Boolean {
        val pwRegex =  "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$+^=!_%*#-?&.])[A-Za-z[0-9]-$@$+!^_%*#?=&.]{8,20}$" // 8 ~ 20자리 사이의 비밀번호. 영문, 숫자, 특수문자 포함
        return !password.isNullOrEmpty() && Pattern.matches(pwRegex, password)
    }

    // 비밀번호 재입력이 입력한 비밀번호와 일치하는지 검사
    fun isSamePassword(original : String, confirm : String) : Boolean {
        return original == confirm
    }

}