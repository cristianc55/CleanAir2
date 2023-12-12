package com.example.cleanair.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cleanair.model.UserModel
import com.example.cleanair.databinding.ActivityLoginScreenBinding
import com.example.cleanair.presenter.LoginPresenter


class LoginScreenActivity : AppCompatActivity() {

    // Create the variables for the views
    private lateinit var binding: ActivityLoginScreenBinding

    // Create the variables for the presenter and model
    private lateinit var loginPresenter: LoginPresenter
    private lateinit var userModel: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the presenter and model
        loginPresenter = LoginPresenter(this)
        userModel = UserModel()

        // Set the onClickListener for the login button
        binding.loginButton.setOnClickListener {
            // Get the username and password from the edit texts
            val username = binding.emailInputEditText.text.toString()
            val password = binding.passwordInputEditText.text.toString()

            // Check if the username and password are valid
            if (username.isEmpty() || password.isEmpty()) {
                this.showToast("Please enter your username and password.")
            } else {
                // Call the login method in the presenter
                loginPresenter.login(username, password)
            }
        }
    }
    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Method to start the MainActivity
    fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}