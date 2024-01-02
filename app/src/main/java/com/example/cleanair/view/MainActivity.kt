package com.example.cleanair.view

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import kotlinx.coroutines.*
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.arcgismaps.ApiKey
import com.arcgismaps.ArcGISEnvironment
import com.arcgismaps.data.Feature
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
import kotlinx.coroutines.flow.collect
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
            binding.mapView.map!!.operationalLayers.clear()
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.air_quality -> {
                    // Respond to navigation item 1 click
                    showToast("AirQuality")
                    val serviceFeatureTable =
                        ServiceFeatureTable("https://services.arcgis.com/cJ9YHowT8TU7DUyn/arcgis/rest/services/Air Now Current Monitor Data Public/FeatureServer/0")

                    val featureLayer = FeatureLayer.createWithFeatureTable(serviceFeatureTable)

                    binding.mapView.map!!.operationalLayers.clear()
                    binding.mapView.map!!.operationalLayers.add(featureLayer)
                    true
                }

                R.id.pm2_5 -> {
                    // Respond to navigation item 2 click
                    showToast("PM_2.5")
                    val serviceFeatureTable =
                        ServiceFeatureTable("https://services9.arcgis.com/RHVPKKiFTONKtxq3/arcgis/rest/services/Air_Quality_PM25_Latest_Results/FeatureServer/0")
                    // create a feature layer with the feature table
                    val featureLayer = FeatureLayer.createWithFeatureTable(serviceFeatureTable)

                    binding.mapView.map!!.operationalLayers.clear()
                    binding.mapView.map!!.operationalLayers.add(featureLayer)
                    binding.mapView.callout

                    this.lifecycleScope.launch {
                        binding.mapView.onSingleTapConfirmed.collect {
                            val identifyResult = binding.mapView.identifyLayers(
                                screenCoordinate = it.screenCoordinate,
                                tolerance = 12.0,
                                returnPopupsOnly = true,
                                maximumResults = 10
                            ).onSuccess {

                            }

//                            identifyResult.onSuccess {result ->
//                               print( result[0].popups)
//                            }
                        }
                    }

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
                    val serviceFeatureTable =
                        ServiceFeatureTable("https://services.arcgis.com/cJ9YHowT8TU7DUyn/arcgis/rest/services/FRS_INTERESTS_NPDES_MAJOR/FeatureServer/0")

                    val featureLayer = FeatureLayer.createWithFeatureTable(serviceFeatureTable)

                    binding.mapView.map!!.operationalLayers.clear()
                    binding.mapView.map!!.operationalLayers.add(featureLayer)
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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}