package com.example.locationapp.models

import java.io.Serializable

data class Coordinates(val _id: String,
                       val longitude: Double,
                       val latitude: Double): Serializable
