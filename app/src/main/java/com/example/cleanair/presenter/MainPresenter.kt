package com.example.cleanair.presenter

import com.example.cleanair.view.MainActivity
import com.google.firebase.auth.FirebaseAuth

class MainPresenter(private val mainActivity: MainActivity) {

    fun checkLoginStatus(auth: FirebaseAuth): Boolean = (auth.currentUser == null)
}