package com.example.cleanair

import android.app.Application
import com.google.android.material.color.DynamicColors;
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

//TODO: Move certain objects in here that need to be independent of the MainActivity lifecycle
//Firebase.Auth might be one of them
class CleanAirApplication : Application() {
    companion object {
        val ARCGIS_API_KEY = "AAPK5b0c423bfa0c428db6369d72a4f1bb4eT6naFXE4Ke3zPZ6Y9tqJVFrehh6IcF7rlteFR1lDs9Y2d6DHmxuc5YKkz3RoxJuu"
        val geocodeServer = "https://geocode-api.arcgis.com/arcgis/rest/services/World/GeocodeServer"
        val database = Firebase.database("https://clean-air-isi-viewmodel-default-rtdb.europe-west1.firebasedatabase.app/")
        val reference: DatabaseReference = database.getReference("users")
        val referenceUserPoints: DatabaseReference = database.getReference("userPoints")
    }
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this);
    }
}