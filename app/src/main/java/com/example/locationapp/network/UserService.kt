package com.example.locationapp.network

import com.example.locationapp.models.UserModel
import com.example.locationapp.models.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("/user/add-user")
    fun postUser(@Body user: UserModel): Call<UserResponse>
}