package com.example.cleanair.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cleanair.databinding.ActivitySignUpScreenBinding
import com.example.cleanair.presenter.SignUpPresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpScreenBinding

    private lateinit var signUpPresenter: SignUpPresenter

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        signUpPresenter = SignUpPresenter(this)

        // Initialize the FirebaseAuth instance
        auth = Firebase.auth

        binding.signUpButton.setOnClickListener {
            val email = binding.emailSignUpEditText.text.toString()
            val username = binding.usernameSignUpEditText.text.toString()
            val password = binding.passwordSignUpEditText.text.toString()

            signUpPresenter.signUp(email, username, password, auth)
        }
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, LoginScreenActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun startLoginActivity() {
        val intent = Intent(this, LoginScreenActivity::class.java)
        startActivity(intent)
        finish()
    }
}