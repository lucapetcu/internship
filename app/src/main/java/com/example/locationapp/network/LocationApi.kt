package com.example.locationapp.network

import com.example.locationapp.models.CoordinatesModel
import com.example.locationapp.models.CoordinatesResponse
import com.example.locationapp.models.LastCoordinatesModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface LocationApi {
    @POST("/coordinates/receive-coordinates")
    fun sendCoordinates(@Body coordinates: CoordinatesModel): Call<CoordinatesResponse>

    @GET("/coordinates/get-last-coordinates/{username}")
    suspend fun getLastCoordinates(@Path("username") username: String): Response<LastCoordinatesModel>
}