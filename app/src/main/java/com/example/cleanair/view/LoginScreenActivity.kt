package com.example.cleanair.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cleanair.databinding.ActivityLoginScreenBinding
import com.example.cleanair.presenter.LoginPresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginScreenActivity : AppCompatActivity() {

    // Create the variables for the views
    private lateinit var binding: ActivityLoginScreenBinding

    // Create the variables for the presenter and model
    private lateinit var loginPresenter: LoginPresenter

    // Create the FirebaseAuth instance
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Initialize the presenter and model
        loginPresenter = LoginPresenter(this)

        // Initialize the FirebaseAuth instance
        auth = Firebase.auth

        // Set the onClickListener for the login button
        binding.loginButton.setOnClickListener {
            // Get the username and password from the edit texts
            val email = binding.emailInputEditText.text.toString()
            val password = binding.passwordInputEditText.text.toString()

            loginPresenter.login(email, password, Firebase.auth)
        }

        binding.signUpLoginButton.setOnClickListener {
            startSignUpActivity()
        }
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun startSignUpActivity() {
        val intent = Intent(this, SignUpScreenActivity::class.java)
        startActivity(intent)
        finish()
    }
}