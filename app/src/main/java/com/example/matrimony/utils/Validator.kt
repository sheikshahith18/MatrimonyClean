package com.example.matrimony.utils

object Validator {

    fun mobileNumValidator(mobile: String): Boolean {
        return Regex("^[6-9]\\d{9}$").matches(mobile)
    }

    fun emailAddressValidator(email: String): Boolean {
        return Regex("^\\w{3,}\\.?\\w*@gmail\\.com$").matches(email)
    }

    fun strongPasswordValidator(password: String): Boolean {
        return Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}$")
            .matches(password)
    }

    fun confirmPasswordValidator(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }

}