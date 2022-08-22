package com.example.locationapp.models

import java.io.Serializable

data class User(val _id: String,
                val user_email: String,
                val user_name: String): Serializable
