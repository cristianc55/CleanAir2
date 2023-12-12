package com.example.cleanair.presenter

import android.content.ContentValues.TAG
import android.util.Log
import com.example.cleanair.model.UserModel
import com.example.cleanair.view.LoginScreenActivity
import com.google.firebase.auth.FirebaseAuth

class LoginPresenter(private val loginActivity: LoginScreenActivity) {
    fun login(email: String, password: String, auth: FirebaseAuth) {
        // Check if the email and password are empty
        if (email.isEmpty() || password.isEmpty()) {
            loginActivity.showToast("Please enter your username and password.")
        } else {
            // Check the validity of the email
            if (UserModel.isEmailValid(email)) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(loginActivity) { task ->
                        if (task.isSuccessful) {
                            // Sign in success case
                            Log.d(TAG, "signInWithEmail:success")
                            //TODO save some of this info in SharedPreferences
                            val user = auth.currentUser
                            loginActivity.startMainActivity()
                        } else {
                            // Sign in fail case
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            loginActivity.showToast("Failed Authentication")
                        }
                    }
            }
        }
    }
}