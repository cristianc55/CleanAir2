package com.example.cleanair.presenter

import com.arcgismaps.geometry.Point
import com.arcgismaps.geometry.SpatialReference
import com.example.cleanair.CleanAirApplication
import com.example.cleanair.data.SerializablePoint
import com.example.cleanair.data.toPoint
import com.example.cleanair.data.toSerializablePoint
import com.example.cleanair.view.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainPresenter(private val mainActivity: MainActivity) {

    fun checkLoginStatus(auth: FirebaseAuth): Boolean = (auth.currentUser == null)

    fun addPointToDatabase(point: Point): Boolean {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid.isNullOrEmpty()) {
            return false
        } else {
            CleanAirApplication.reference.child(uid).get().addOnSuccessListener {
                if (it.hasChild("points")) {
                    val jsonPoints = it.child("points").value
                    val type = object : TypeToken<List<SerializablePoint>>() {}.type
                    val serializablePoints: MutableList<SerializablePoint> =
                        Gson().fromJson<MutableList<SerializablePoint>?>(
                            jsonPoints.toString(),
                            type
                        ).toMutableList()

                    serializablePoints.add(point.toSerializablePoint())
                    val updatedJsonPoints = Gson().toJson(serializablePoints)
                    CleanAirApplication.reference.child(uid).child("points")
                        .setValue(updatedJsonPoints)
                    CleanAirApplication.reference.push()
                } else {
                    val points: ArrayList<Point> = arrayListOf(point)
                    val serializablePoints = points.map { it.toSerializablePoint() }
                    val gson = Gson()
                    val jsonPoints = gson.toJson(serializablePoints)
                    CleanAirApplication.reference.child(uid).child("points").setValue(jsonPoints)
                    CleanAirApplication.reference.push()
                }
            }.addOnFailureListener {
                println("hiii")
            }



            CleanAirApplication.referenceUserPoints.get().addOnSuccessListener {
                if (it.hasChild("points")) {
                    val jsonPoints = it.child("points").value
                    val type = object : TypeToken<List<SerializablePoint>>() {}.type
                    val serializablePoints: MutableList<SerializablePoint> =
                        Gson().fromJson<MutableList<SerializablePoint>?>(
                            jsonPoints.toString(),
                            type
                        ).toMutableList()
                    serializablePoints.add(point.toSerializablePoint())
                    val updatedJsonPoints = Gson().toJson(serializablePoints)
                    CleanAirApplication.referenceUserPoints.child("points")
                        .setValue(updatedJsonPoints)
                    CleanAirApplication.referenceUserPoints.push()
                }
            }.addOnFailureListener {
                println("hiii")
            }
        }
        return true
    }

    fun getUserPoints(): List<Point> {
        var userPoints: List<Point> = emptyList()
        CleanAirApplication.referenceUserPoints.get().addOnSuccessListener {
            if (it.hasChild("points")) {
                val jsonPoints = it.child("points").value
                val type = object : TypeToken<List<SerializablePoint>>() {}.type
                val serializablePoints: MutableList<SerializablePoint> =
                    Gson().fromJson<MutableList<SerializablePoint>?>(
                        jsonPoints.toString(),
                        type
                    ).toMutableList()
                userPoints = serializablePoints.map { it.toPoint() }
                mainActivity.drawUserPoints(userPoints)
            }
        }.addOnFailureListener {
            println("hiii")
        }

        return userPoints
    }
}