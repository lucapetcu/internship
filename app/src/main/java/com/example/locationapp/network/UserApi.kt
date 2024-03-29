package com.example.locationapp.network

import com.example.locationapp.models.UserListResponse
import com.example.locationapp.models.UserModel
import com.example.locationapp.models.UserResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApi {
    @POST("/user/add-user")
    fun postUser(@Body user: UserModel): Call<UserResponse>

    @GET("/user/get-users-without/{userToken}")
    suspend fun getAllUsers(@Path("userToken") userToken: String): Response<UserListResponse>
}