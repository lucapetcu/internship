package com.example.locationapp.models

import java.io.Serializable

data class LastCoordinatesModel(val status: String,
                                val locations: ArrayList<Coordinates>): Serializable
