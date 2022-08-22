package com.example.locationapp.models

import java.io.Serializable

data class UserListResponse(val status: String,
                            val users: ArrayList<User>): Serializable
