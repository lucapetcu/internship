package com.example.locationapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.locationapp.databinding.ActivityMenuBinding
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth

class MenuActivity : AppCompatActivity() {

    private var latitude: Double = -1.0
    private var longitude: Double = -1.0

    private var binding: ActivityMenuBinding? = null
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        binding?.btnshowCurrentLocation?.setOnClickListener {
            Log.e("Clicked", "Show location button clicked")
            Log.e("Clicked", "Show location button clicked")
            if (isLocationEnabled()) {
                Log.e("Location", "Location enabled")
                requestNewLocationData()
                Log.e("Current Latitude", "$latitude")
                Log.e("Current Longitude", "$longitude")
            } else {
                Log.e("Location error", "Location not active")
            }

            val intent = Intent(this, ShowLocationActivity::class.java)
            startActivity(intent)
        }

        binding?.btnLogout?.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            finish()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
//        val mLocationRequest = LocationRequest.create().apply {
//            interval = 0
//            fastestInterval = 0
//            priority = Priority.PRIORITY_HIGH_ACCURACY
//            numUpdates = 1
//        }
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
    }
    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location ?= locationResult.lastLocation
            latitude = mLastLocation!!.latitude
            Log.e("Current Latitude ca", "$latitude")
            longitude = mLastLocation.longitude
            Log.e("Current Longitude ca", "$longitude")
        }
    }

}