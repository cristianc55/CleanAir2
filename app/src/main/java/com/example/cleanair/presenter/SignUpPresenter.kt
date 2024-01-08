package com.example.cleanair.presenter

import android.content.ContentValues.TAG
import android.util.Log
import com.arcgismaps.geometry.Point
import com.example.cleanair.CleanAirApplication
import com.example.cleanair.data.UserDataClass
import com.example.cleanair.data.toSerializablePoint
import com.example.cleanair.model.UserModel
import com.example.cleanair.view.SignUpScreenActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson

class SignUpPresenter(private val signupActivity: SignUpScreenActivity) {

    fun signUp(email: String, username: String, password: String, auth: FirebaseAuth) {
        if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
            signupActivity.showToast("Please enter your email, username and password.")
        } else {
            if (UserModel.isEmailValid(email)) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(signupActivity) { task ->
                        if (task.isSuccessful) {
                            // Sign up success
                            Log.d(TAG, "createUserWithEmail:success")
                            //TODO save some of this info in SharedPreferences
                            val user = auth.currentUser
                            val points: ArrayList<Point> = arrayListOf(Point(22.22, 22.22))
                            val serializablePoints = points.map { it.toSerializablePoint() }
                            val gson = Gson()
                            val defaultPoints = gson.toJson(serializablePoints)
                            val userData = UserDataClass(user!!.uid ,username ,email , defaultPoints)
                            CleanAirApplication.reference.child(user.uid).setValue(userData).addOnSuccessListener {
                                //TODO
                                println("yesssssssssssss")
                            }.addOnFailureListener{
                                println("nooooooooooooooo")
                            }
                            CleanAirApplication.reference.push()

                            signupActivity.startLoginActivity()
                        } else {
                            // If sign up fail
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            signupActivity.showToast("Authentication failed.")
                        }
                    }
            }
        }
    }
}