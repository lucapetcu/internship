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
        return iBinder
    }

    override fun onRebind(intent: Intent?) {
        stopForeground(true)
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        startForeground(NOTIFICATION_ID, generateForegroundNotifications())
        return true
    }


    override fun onCreate() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NotificationManager::class.java)
            val notificationChannel = NotificationChannel(CHANNEL_ID, "Foreground service",
                NotificationManager.IMPORTANCE_HIGH)

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action != null && intent.action.equals("stop")) {
            removeLocationRequest()
            stopForeground(true)
            stopSelf()
        } else {
            generateForegroundNotifications()
        }

        return START_NOT_STICKY
    }

    fun removeLocationRequest() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
    }

    @SuppressLint("MissingPermission")
    fun requestNewLocationData() {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val locationRequest = LocationRequest.create()?.apply {
            interval = Utils.getInterval(applicationContext)
            fastestInterval = Utils.getFastestInterval(applicationContext)
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }


        mFusedLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper())

    }

    private fun generateForegroundNotifications(): Notification {
        val intent = Intent(this, SettingsActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(this)
        notificationBuilder.setContentTitle("This is the title")
            .setContentText("This is the text")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setWhen(System.currentTimeMillis())
            .setOngoing(true)
            .setContentIntent(pendingIntent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder.setChannelId(CHANNEL_ID)
        }
        return notificationBuilder.build()
    }

    inner class LocalBinder : Binder() {
        fun getService() : LocationServiceUpdates {
            return this@LocationServiceUpdates
        }
    }

}