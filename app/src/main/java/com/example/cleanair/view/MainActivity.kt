package com.example.cleanair.view

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import kotlinx.coroutines.*
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.arcgismaps.ApiKey
import com.arcgismaps.ArcGISEnvironment
import com.arcgismaps.data.Feature
import com.arcgismaps.data.ServiceFeatureTable
import com.arcgismaps.geometry.GeometryEngine
import com.arcgismaps.geometry.Point
import com.arcgismaps.mapping.ArcGISMap
import com.arcgismaps.mapping.BasemapStyle
import com.arcgismaps.mapping.Viewpoint
import com.arcgismaps.mapping.layers.FeatureLayer
import com.arcgismaps.mapping.symbology.PictureMarkerSymbol
import com.arcgismaps.mapping.view.Graphic
import com.arcgismaps.mapping.view.GraphicsOverlay
import com.arcgismaps.tasks.geocode.LocatorTask
import com.example.cleanair.CleanAirApplication
import com.example.cleanair.R
import com.example.cleanair.databinding.ActivityMainBinding
import com.example.cleanair.presenter.MainPresenter
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var auth: FirebaseAuth

    private lateinit var presenter: MainPresenter

    private var addWaypoints = false

    private var showUsersWaypoints = false

    // create a graphics overlay
    private val graphicsOverlay = GraphicsOverlay()

    // set the pin graphic for tapped location
    private val pinSymbol by lazy {
        createPinSymbol()
    }

    private val pinSymbolUsers by lazy {
        createPinSymbolUsers()
    }

    // locator task to provide geocoding services
    private val locatorTask = LocatorTask(CleanAirApplication.geocodeServer)

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

            binding.mapView.graphicsOverlays.add(graphicsOverlay)
        }

        binding.resetViewpointButton.setOnClickListener {
            binding.mapView.setViewpoint(Viewpoint(29.7604, -95.3698, 1000000.0))
            binding.mapView.map!!.operationalLayers.clear()
        }

        //Alberto
        binding.usersWaypointsButton.setOnClickListener {
            showUsersWaypoints = showUsersWaypoints.not()
            if (showUsersWaypoints) {
                presenter.getUserPoints()
            } else {
                graphicsOverlay.graphics.clear()
            }
        }

        //Alberto
        binding.addWaypointButton.setOnClickListener {
            if (binding.addWaypointButton.isPressed && addWaypoints.not()) {
                binding.addWaypointButton.setImageResource(R.drawable.add_circle_fill1_wght400_grad0_opsz24)
                addWaypoints = true
            } else {
                addWaypoints = false
                binding.addWaypointButton.setImageResource(R.drawable.add_circle_fill0_wght400_grad0_opsz24)
            }
            binding.mapView.map!!.operationalLayers.clear()
        }

        lifecycleScope.launch {
            // load geocode locator task
            locatorTask.load().onSuccess {
                // locator task loaded, look for geo view tapped
                binding.mapView.onSingleTapConfirmed.collect { event ->
                    event.mapPoint?.let { mapPoint -> geoViewTapped(mapPoint) }
                }
            }.onFailure {
                showError(it.message.toString())
            }
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

    //Alberto
    fun drawUserPoints(points: List<Point>) {
        for (point: Point in points) {
            val pinGraphic = Graphic(point, pinSymbolUsers)
            graphicsOverlay.graphics.add(pinGraphic)
        }
    }

    private suspend fun geoViewTapped(mapPoint: Point) {
        // create graphic for tapped point
        if (addWaypoints) {
            presenter.addPointToDatabase(mapPoint)
        }


        val pinGraphic = Graphic(mapPoint, pinSymbol)
        graphicsOverlay.graphics.apply {
            // clear existing graphics
            clear()
            // add the pin graphic
            add(pinGraphic)
        }
        // normalize the geometry - needed if the user crosses the international date line.
        val normalizedPoint = GeometryEngine.normalizeCentralMeridian(mapPoint) as Point

        // reverse geocode to get address
        locatorTask.reverseGeocode(normalizedPoint).onSuccess { addresses ->
            // get the first result
            val address = addresses.firstOrNull()
            if (address == null) {
                showError("Could not find address at tapped point")
                return@onSuccess
            }
            // use the street and region for the title
            val title = address.attributes["Address"].toString()
            // use the metro area for the description details
            val description = "${address.attributes["City"]} " +
                    "${address.attributes["Region"]} " +
                    "${address.attributes["CountryCode"]}"
            // set the strings to the text views
            showToast(title)
//            titleTV.text = title
//            descriptionTV.text = description
        }.onFailure {
            showError(it.message.toString())
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

    /**
     * Create a picture marker symbol to represent a pin at the tapped location
     */
    private fun createPinSymbol(): PictureMarkerSymbol {
        // get pin drawable
        val pinDrawable = ContextCompat.getDrawable(
            this,
            R.drawable.baseline_location_pin_red_48
        )
        //add a graphic for the tapped point
        val pinSymbol = PictureMarkerSymbol.createWithImage(
            pinDrawable as BitmapDrawable
        )
        pinSymbol.apply {
            // resize the dimensions of the symbol
            width = 50f
            height = 50f
            // the image is a pin so offset the image so that the pinpoint
            // is on the point rather than the image's true center
            leaderOffsetX = 30f
            offsetY = 25f
        }
        return pinSymbol
    }

    private fun createPinSymbolUsers(): PictureMarkerSymbol {
        // get pin drawable
        val pinDrawable = ContextCompat.getDrawable(
            this,
            R.drawable.baseline_location_2
        )
        //add a graphic for the tapped point
        val pinSymbol = PictureMarkerSymbol.createWithImage(
            pinDrawable as BitmapDrawable
        )
        pinSymbol.apply {
            // resize the dimensions of the symbol
            width = 50f
            height = 50f
            // the image is a pin so offset the image so that the pinpoint
            // is on the point rather than the image's true center
            leaderOffsetX = 30f
            offsetY = 25f
        }
        return pinSymbol
    }

    private fun showError(errorMessage: String) {
        Log.e(localClassName, errorMessage)
    }
}