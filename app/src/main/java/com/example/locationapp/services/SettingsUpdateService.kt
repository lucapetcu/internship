package com.example.locationapp.services

import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.locationapp.util.Utils
import com.example.locationapp.domain.SettingsModel
import com.example.locationapp.domain.AppRepository
import com.example.locationapp.models.DeviceTokenModel
import com.example.locationapp.models.DeviceTokenResponse
import com.example.locationapp.network.SettingsApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@AndroidEntryPoint
class SettingsUpdateService: FirebaseMessagingService() {

    @Inject
    lateinit var repo: AppRepository

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        // check if the current user is the intended user
        if (message.data["user_token"]!! == FirebaseAuth.getInstance().currentUser!!.uid) {
            //save received data to Shared Preference
            if (message.data["id"]!! == "settings") {
                Utils.setPreferenceInterval(applicationContext, message.data["interval"]!!.toLong())
                Utils.setPreferenceFastestInterval(applicationContext, message.data["fastest_interval"]!!.toLong())
                repo.insertSettings(
                    SettingsModel(message.data["interval"]!!.toLong(),
                        message.data["fastest_interval"]!!.toLong())
                )
            } else {
                if (message.data["start"]!! == "start") {
                    Utils.setButtonState(applicationContext, true)
                    val intent = Intent(this, LocationServiceUpdates::class.java)
                    startForegroundService(intent)
                } else {
                    Utils.setButtonState(applicationContext, false)
                    val intent = Intent(this, LocationServiceUpdates::class.java)
                    intent.putExtra("stop", "stop")
                    stopService(intent)
                }
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i("FCM", token)
        sendTokenToServer(token)
    }

    private fun sendTokenToServer(token: String) {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val settingsService: SettingsApi = retrofit.create(SettingsApi::class.java)

        val newDeviceToken = DeviceTokenModel(token, FirebaseAuth.getInstance().currentUser!!.uid)

        val call: Call<DeviceTokenResponse> = settingsService.sendDeviceToken(newDeviceToken)

        call.enqueue(object : Callback<DeviceTokenResponse> {
            override fun onResponse(call: Call<DeviceTokenResponse>, response: Response<DeviceTokenResponse>) {
                val settingsResponse: DeviceTokenResponse = response.body()!!
                Log.i("Response result", "$settingsResponse")
            }

            override fun onFailure(call: Call<DeviceTokenResponse>, t: Throwable) {
                Log.e("retrofit", "error " + t.message)
            }
        })
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }
}