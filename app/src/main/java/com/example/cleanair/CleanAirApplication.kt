package com.example.cleanair

import android.app.Application
import com.google.android.material.color.DynamicColors;

//TODO: Move certain objects in here that need to be independent of the MainActivity lifecycle
//Firebase.Auth might be one of them
class CleanAirApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this);
    }
}