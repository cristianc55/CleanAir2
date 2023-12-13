package com.example.cleanair

import android.app.Application
import com.google.android.material.color.DynamicColors;

//TODO: Move certain objects in here that need to be independent of the MainActivity lifecycle
//Firebase.Auth might be one of them
class CleanAirApplication : Application() {
    companion object {
        val ARCGIS_API_KEY = "AAPK5b0c423bfa0c428db6369d72a4f1bb4eT6naFXE4Ke3zPZ6Y9tqJVFrehh6IcF7rlteFR1lDs9Y2d6DHmxuc5YKkz3RoxJuu"
    }
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this);
    }
}