package com.example.cleanair

import android.util.Patterns

class UserModel(
    private val usernameKey: String = "username",
    private val email: String = "email",
    private val password: String = "password"
) {

    fun isEmailValid(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()
}