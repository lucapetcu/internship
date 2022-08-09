package com.example.locationapp.models

import java.io.Serializable

data class UserResponse(val status: String,
                        val userName: String,
                        val userEmail: String): Serializable
