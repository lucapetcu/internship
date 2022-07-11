package com.example.locationapp

import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.content.res.Configuration
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import java.security.AccessController.getContext
import java.util.*

class LocationServiceUpdates: Service() {

    private var CHANNEL_ID: String = "LocationServiceChannel"
    private var NOTIFICATION_ID: Int = 123
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    private var iBinder: IBinder = LocalBinder()


    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location? = locationResult.lastLocation
            val latitude = mLastLocation!!.latitude
            Log.i("service_lat", "$latitude")
            val longitude = mLastLocation.longitude
            Log.i("service_log", "$longitude")
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.i("in bind", "in on bind")
        stopForeground(true)
        requestNewLocationData()
        return iBinder
    }

    override fun onRebind(intent: Intent?) {
        Log.i("in bind", "in on rebind")
        stopForeground(true)
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.i("in bind", "in on unbind")
        startForeground(NOTIFICATION_ID, getNotification())
        return true
    }


    override fun onCreate() {
        Log.i("On create", "in on create")
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NotificationManager::class.java) as NotificationManager
            val notificationChannel = NotificationChannel(CHANNEL_ID, "Foreground service",
                NotificationManager.IMPORTANCE_DEFAULT)

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("in start", "in on start command")
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

        //startService(Intent(applicationContext, LocationServiceUpdates::class.java))
        mFusedLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper())

    }

    private fun getNotification(): Notification {
        val intent = Intent(this, SettingsActivity::class.java)
        intent.putExtra("notification", true)
        val pendingIntent = PendingIntent.getActivity(this, 0,
            intent, PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
        notificationBuilder.setContentTitle("This is the title")
            .setContentText("This is the text")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
        return notificationBuilder.build()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("in destroy", "in on destroy")
        removeLocationRequest()
    }

    inner class LocalBinder : Binder() {
        fun getService() : LocationServiceUpdates {
            return this@LocationServiceUpdates
        }
    }

}