package com.example.locationapp

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.locationapp.databinding.ActivitySettingsBinding
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : AppCompatActivity() {
    private var binding: ActivitySettingsBinding? = null

    private var mService: LocationServiceUpdates? = null

    private var LOCATION_REQUEST_CODE = 101
    private var isBound: Boolean = false

    private var mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as LocationServiceUpdates.LocalBinder
            mService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mService = null
            isBound = false
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        if (Utils.getInterval(applicationContext) != -1L) {
            binding?.setInterval?.setText(Utils.getInterval(applicationContext).toString())
        }
        if (Utils.getFastestInterval(applicationContext) != -1L) {
            binding?.setFastestInterval?.setText(Utils.getFastestInterval(applicationContext).toString())
        }

        binding?.toggleSwitch?.isChecked = Utils.getButtonState(applicationContext)


        binding?.toggleSwitch?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Utils.setPreferenceInterval(applicationContext, binding?.setInterval?.text.toString().toLong())
                Utils.setPreferenceFastestInterval(applicationContext, binding?.setFastestInterval?.text.toString().toLong())
                Utils.setButtonState(applicationContext, isChecked)
                //check location permissions here
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        Log.i("Alert dialog location", "should show an alert dialog here...")
                        ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            LOCATION_REQUEST_CODE)
                    } else {
                        ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            LOCATION_REQUEST_CODE)
                    }
                    return@setOnCheckedChangeListener
                }
                //start foreground service here
                val intent = Intent(this, LocationServiceUpdates::class.java)
                startForegroundService(intent)
            } else {
                Utils.setButtonState(applicationContext, isChecked)
                //stop foreground service
                val intent = Intent(this, LocationServiceUpdates::class.java)
                intent.putExtra("stop", "stop")
                stopService(intent)
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //start foreground service
                val intent = Intent(this, LocationServiceUpdates::class.java)
                startForegroundService(intent)
            } else {
                //permission denied
            }
        }
    }

//    override fun onStart() {
//        super.onStart()
//        bindService(Intent(this, LocationServiceUpdates::class.java),
//            mServiceConnection,
//            Context.BIND_AUTO_CREATE)
//    }
//
//    override fun onStop() {
//        super.onStop()
//        if (isBound) {
//            unbindService(mServiceConnection)
//        }
//    }
}