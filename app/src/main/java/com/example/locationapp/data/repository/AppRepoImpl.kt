package com.example.locationapp.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.locationapp.data.local.SettingsDatabase
import com.example.locationapp.data.mapper.toSettingsEntity
import com.example.locationapp.data.mapper.toSettingsModel
import com.example.locationapp.domain.SettingsModel
import com.example.locationapp.domain.AppRepository
import com.example.locationapp.models.*
import com.example.locationapp.network.LocationApi
import com.example.locationapp.network.SettingsApi
import com.example.locationapp.network.UserApi
import com.example.locationapp.util.Utils
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepoImpl @Inject constructor(
    db: SettingsDatabase,
    private val locApi: LocationApi,
    private val settApi: SettingsApi,
    private val userApi: UserApi,
    @ApplicationContext private val app: Context): AppRepository {
    private val dao = db.settingsDao()
    override fun getSettings(): LiveData<SettingsModel> {
        return Transformations.map(dao.getSettings()) {
            it?.toSettingsModel()
        }
    }

    override fun insertSettings(settingsModel: SettingsModel) {
        dao.deleteSettings()
        dao.insertSettings(settingsModel.toSettingsEntity())
    }

    override fun sendCoordinates(token: String, longitude: Double, latitude: Double) {
        val coordinatesModel = CoordinatesModel(token, latitude, longitude)
        val call: Call<CoordinatesResponse> = locApi.sendCoordinates(coordinatesModel)

        call.enqueue(object : Callback<CoordinatesResponse> {
            override fun onResponse(
                call: Call<CoordinatesResponse>,
                response: Response<CoordinatesResponse>
            ) {
                val coordResponse = response.body()!!
                Log.i("Response result coord", "$coordResponse")
            }

            override fun onFailure(call: Call<CoordinatesResponse>, t: Throwable) {
                Log.e("retrofit coord", "error " + t.message)
            }
        })
    }

    override suspend fun getLastCoordinates(username: String): ArrayList<Coordinates> {
        val result = locApi.getLastCoordinates(username)
        return result.body()!!.locations
    }

    override fun getUsername(): String {
        return Utils.getUsername(app)
    }

    override fun postUserToServer(email: String, name: String, token: String) {
        val newUser = UserModel(token, email, name)

        val call: Call<UserResponse> = userApi.postUser(newUser)

        call.enqueue(object : Callback<UserResponse>{
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                val userResponse: UserResponse = response.body()!!
                Log.i("Response result", "$userResponse")
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e("retrofit", "error " + t.message)
            }
        })
    }

    override fun sendDeviceToken(deviceToken: String, userToken: String) {
        val newDeviceToken = DeviceTokenModel(deviceToken, userToken)

        val call: Call<DeviceTokenResponse> = settApi.sendDeviceToken(newDeviceToken)

        call.enqueue(object : Callback<DeviceTokenResponse>{
            override fun onResponse(call: Call<DeviceTokenResponse>, response: Response<DeviceTokenResponse>) {
                val settingsResponse: DeviceTokenResponse = response.body()!!
                Log.i("Response result", "$settingsResponse")
            }

            override fun onFailure(call: Call<DeviceTokenResponse>, t: Throwable) {
                Log.e("retrofit", "error " + t.message)
            }
        })
    }

    override suspend fun getAllUsers(userToken: String): ArrayList<User> {
        val result = userApi.getAllUsers(userToken)
        return result.body()!!.users
    }
}