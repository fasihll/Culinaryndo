package com.example.culinaryndo.ui.maps

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.culinaryndo.R
import com.example.culinaryndo.ViewModelFactory
import com.example.culinaryndo.data.Result
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.culinaryndo.databinding.ActivityMapsBinding
import com.example.culinaryndo.ui.home.HomeViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var placesClient: PlacesClient
    private val boundsBuilder = LatLngBounds.Builder()
    private lateinit var binding: ActivityMapsBinding
    private val viewModel by viewModels<MapsViewModel> {
        ViewModelFactory.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val foodname = intent.getStringExtra(FOOD_NAME).toString()
        val latitude = intent.getDoubleExtra(MY_LOCATION_LATITUDE, 0.0)
        val logntitude = intent.getDoubleExtra(MY_LOCATION_LONGTITUDE,0.0)

        Log.d("MYLOCATION_MAPS",LatLng(latitude,logntitude).toString())

        viewModel.location.value = LatLng(latitude,logntitude)
        viewModel.foodname.value = foodname

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        addManyPlace()

        getMyLocation()
    }

    private fun addManyPlace() {
        val location = viewModel.location.value as LatLng
        val radius = 500
        val type = "restaurant"
        val keyword = viewModel.foodname.value.toString()
        viewModel.getNarestPlaceFood(location,radius.toString(),type,keyword).observe(this){
            result ->
            if (result != null){
                when(result){
                    is Result.Loading -> {
                        setLoading(true)
                    }
                    is Result.Success -> {
                        if (result.data.status == "OK"){
                            result.data.results?.forEach{data ->
                                Log.d("POSITION_MAPS", LatLng(
                                    data?.geometry?.location?.lat!!.toDouble(),
                                    data.geometry.location.lng!!.toDouble()).toString()
                                )
                                mMap.addMarker(
                                    MarkerOptions()
                                        .position(
                                            LatLng(
                                                data.geometry.location.lat.toDouble(),
                                                data.geometry.location.lng.toDouble()
                                            )
                                        )
                                        .title(data.name)
                                        .snippet(data.vicinity)
                                )
                                boundsBuilder.include(LatLng(
                                    data.geometry.location.lat.toDouble(),
                                    data.geometry.location.lng.toDouble()
                                ))
                            }
                            val bounds: LatLngBounds = boundsBuilder.build()

                            mMap.animateCamera(
                                CameraUpdateFactory.newLatLngBounds(
                                    bounds,
                                    resources.displayMetrics.widthPixels,
                                    resources.displayMetrics.heightPixels,
                                    300
                                )
                            )
                            setLoading(false)
                        }else{
                            setLoading(false)
                            Log.d("ERROR_PLACE_API",result.data.error_message.toString())
                            Toast.makeText(this@MapsActivity,result.data.status,Toast
                                .LENGTH_SHORT).show()
                        }
                    }
                    is Result.Error -> {
                        setLoading(false)
                        Toast.makeText(this@MapsActivity,result.error,Toast
                            .LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }
    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.normal_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }
            R.id.satellite_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }
            R.id.terrain_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }
            R.id.hybrid_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object{
        const val FOOD_NAME = "foodName"
        const val MY_LOCATION_LATITUDE = "latitude"
        const val MY_LOCATION_LONGTITUDE= "longtitude"
    }
}