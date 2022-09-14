package com.example.locationapp.services

import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.locationapp.R
import com.example.locationapp.activities.SettingsActivity
import com.example.locationapp.domain.AppRepository
import com.example.locationapp.util.Utils
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LocationServiceUpdates: Service() {

    @Inject
    lateinit var repo: AppRepository

    private var CHANNEL_ID: String = "LocationServiceChannel"
    private var NOTIFICATION_ID: Int = 123
    private lateinit var mFusedLocationClient: FusedLocationProviderClient


    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location? = locationResult.lastLocation
            val latitude = mLastLocation!!.latitude
            Log.i("service_lat", "$latitude")
            val longitude = mLastLocation.longitude
            Log.i("service_log", "$longitude")
            //send coordinates updates here
            repo.sendCoordinates(FirebaseAuth.getInstance().currentUser!!.uid, longitude, latitude)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NotificationManager::class.java) as NotificationManager
            val notificationChannel = NotificationChannel(CHANNEL_ID, "Foreground service",
                NotificationManager.IMPORTANCE_DEFAULT)

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent!!.hasExtra("stop") && intent.getStringExtra("stop").equals("stop")) {
            removeLocationRequest()
            stopForeground(true)
            stopSelf()
        } else {
            startForeground(NOTIFICATION_ID, getNotification())
            requestNewLocationData()
        }
        return START_STICKY
    }

    private fun removeLocationRequest() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val locationRequest = LocationRequest.create()?.apply {
            interval = Utils.getInterval(applicationContext)
            fastestInterval = Utils.getFastestInterval(applicationContext)
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }

        mFusedLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper())
    }

    private fun getNotification(): Notification {
        val intent = Intent(this, SettingsActivity::class.java)
        intent.putExtra("notification", true)
        val pendingIntent = PendingIntent.getActivity(this, 0,
            intent, PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
        notificationBuilder.setContentTitle("LocationApp")
            .setContentText("Foreground service is running")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setSmallIcon(R.drawable.location)
        return notificationBuilder.build()
    }

    override fun onDestroy() {
        super.onDestroy()
        removeLocationRequest()
    }
}