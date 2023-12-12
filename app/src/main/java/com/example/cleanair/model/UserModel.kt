package com.example.cleanair.model

import android.util.Patterns

class UserModel(
    private val usernameKey: String = "username",
    private val email: String = "email",
    private val password: String = "password"
) {
    companion object UserCompanion {
        fun isEmailValid(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}