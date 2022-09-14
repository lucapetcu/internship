package com.example.locationapp.domain

import androidx.lifecycle.LiveData
import com.example.locationapp.models.Coordinates
import com.example.locationapp.models.User

interface AppRepository {
    fun getSettings(): LiveData<SettingsModel>
    fun insertSettings(settingsModel: SettingsModel)
    fun sendCoordinates(token: String, longitude: Double, latitude: Double)
    suspend fun getLastCoordinates(username: String): ArrayList<Coordinates>
    fun getUsername(): String
    fun postUserToServer(email: String, name: String, token: String)
    fun sendDeviceToken(deviceToken: String, userToken: String)
    suspend fun getAllUsers(userToken: String): ArrayList<User>
}