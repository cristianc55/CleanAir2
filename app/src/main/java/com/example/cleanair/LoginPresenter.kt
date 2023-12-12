package com.example.cleanair

class LoginPresenter(private val loginActivity: LoginScreenActivity) {
    // Method to login
    fun login(username: String, password: String) {
        // Check if the username and password are valid
        if (username.isEmpty() || password.isEmpty()) {
            loginActivity.showToast("Please enter your username and password.")
        } else {
            //TODO
        }
    }
}