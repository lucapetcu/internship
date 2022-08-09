package com.example.locationapp.models

import java.io.Serializable

data class CoordinatesModel(val userToken: String,
                            val longitude: Double,
                            val latitude: Double): Serializable
