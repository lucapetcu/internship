package com.example.locationapp.models

import java.io.Serializable

data class CoordinatesResponse(val status: String,
                               val latitude: Double,
                               val longitude: Double): Serializable
