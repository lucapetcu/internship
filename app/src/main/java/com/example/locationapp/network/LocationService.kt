package com.example.locationapp.network

import com.example.locationapp.models.CoordinatesModel
import com.example.locationapp.models.CoordinatesResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LocationService {
    @POST("/coordinates/receive-coordinates")
    fun sendCoordinates(@Body coordinates: CoordinatesModel): Call<CoordinatesResponse>
}