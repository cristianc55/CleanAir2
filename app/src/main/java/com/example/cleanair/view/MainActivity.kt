package com.example.cleanair.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.arcgismaps.ApiKey
import com.arcgismaps.ArcGISEnvironment
import com.arcgismaps.mapping.ArcGISMap
import com.arcgismaps.mapping.BasemapStyle
import com.arcgismaps.mapping.Viewpoint
import com.example.cleanair.CleanAirApplication
import com.example.cleanair.databinding.ActivityMainBinding
import com.example.cleanair.presenter.MainPresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var auth: FirebaseAuth

    private lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Initialize the FirebaseAuth instance
        auth = Firebase.auth
        presenter = MainPresenter(this)

        // authentication with an API key or named user is
        // required to access basemaps and other location services
        ArcGISEnvironment.apiKey = ApiKey.create(CleanAirApplication.ARCGIS_API_KEY)

        lifecycle.addObserver(binding.mapView)

        val map = ArcGISMap(BasemapStyle.ArcGISNavigationNight)

        binding.mapView.map = map

        binding.mapView.map?.apply {
            initialViewpoint = Viewpoint(29.7604, -95.3698, 1000000.0)
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        if (presenter.checkLoginStatus(auth)) {
            startLoginActivity()
        }
    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginScreenActivity::class.java)
        startActivity(intent)
        finish()
    }
}