package com.example.locationapp.network

import com.example.locationapp.models.DeviceTokenModel
import com.example.locationapp.models.DeviceTokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SettingsApi {
    @POST("/settings/receive-token")
    fun sendDeviceToken(@Body deviceToken: DeviceTokenModel): Call<DeviceTokenResponse>
}