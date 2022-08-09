package com.example.locationapp.models

import java.io.Serializable

data class UserModel(val userToken: String,
                     val userEmail: String,
                     val userName: String): Serializable
