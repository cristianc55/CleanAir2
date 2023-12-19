package com.example.cleanair.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.arcgismaps.ApiKey
import com.arcgismaps.ArcGISEnvironment
import com.arcgismaps.data.ServiceFeatureTable
import com.arcgismaps.mapping.ArcGISMap
import com.arcgismaps.mapping.BasemapStyle
import com.arcgismaps.mapping.Viewpoint
import com.arcgismaps.mapping.layers.FeatureLayer
import com.example.cleanair.CleanAirApplication
import com.example.cleanair.R
import com.example.cleanair.databinding.ActivityMainBinding
import com.example.cleanair.presenter.MainPresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

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

        binding.resetViewpointButton.setOnClickListener {
            binding.mapView.setViewpoint(Viewpoint(29.7604, -95.3698, 1000000.0))
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.air_quality -> {
                    // Respond to navigation item 1 click
                    showToast("AirQuality")
                    true
                }

                R.id.pm2_5 -> {
                    // Respond to navigation item 2 click
                    showToast("PM_2.5")
                    val serviceFeatureTable =
                        ServiceFeatureTable("https://services.arcgis.com/jIL9msH9OI208GCb/arcgis/rest/services/USA_PM25_1998_to_2016/FeatureServer")
                    // create a feature layer with the feature table
                    val featureLayer = FeatureLayer.createWithFeatureTable(serviceFeatureTable)

                    binding.mapView.map!!.operationalLayers.clear()
                    binding.mapView.map!!.operationalLayers.add(featureLayer)
                    true
                }

                R.id.national_parks -> {
                    // Respond to navigation item 3 click
                    showToast("National Parks")
                    val serviceFeatureTable =
                        ServiceFeatureTable("https://services.arcgis.com/P3ePLMYs2RVChkJx/arcgis/rest/services/USA_Parks/FeatureServer/0")
                    // create a feature layer with the feature table
                    val featureLayer = FeatureLayer.createWithFeatureTable(serviceFeatureTable)

                    binding.mapView.map!!.operationalLayers.clear()
                    binding.mapView.map!!.operationalLayers.add(featureLayer)
                    true
                }

                R.id.pollution -> {
                    // Respond to navigation item 4 click
                    showToast("Pollution")
                    true
                }
            }
            return@setOnItemSelectedListener true
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

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}