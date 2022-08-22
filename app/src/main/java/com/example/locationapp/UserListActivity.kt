package com.example.locationapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.locationapp.adapters.UserAdapter
import com.example.locationapp.databinding.ActivityUserListBinding
import com.example.locationapp.models.User
import com.example.locationapp.models.UserListResponse
import com.example.locationapp.network.UserService
import com.google.firebase.auth.FirebaseAuth
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class UserListActivity : AppCompatActivity() {
    private var binding: ActivityUserListBinding? = null
    private var userList: ArrayList<UserListResponse> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        getAllUsers()
    }

    private fun getAllUsers() {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val userService: UserService = retrofit.create(UserService::class.java)

        val call: Call<UserListResponse> = userService.getAllUsers(FirebaseAuth.getInstance().currentUser!!.uid)

        call.enqueue(object : Callback<UserListResponse> {
            override fun onResponse(
                call: Call<UserListResponse>,
                response: Response<UserListResponse>
            ) {
                val adapter = UserAdapter(this@UserListActivity, response.body()!!.users)

                binding?.rvUsersList?.adapter = adapter
                binding?.rvUsersList?.layoutManager = LinearLayoutManager(this@UserListActivity)
            }

            override fun onFailure(call: Call<UserListResponse>, t: Throwable) {
                Log.e("retrofit getAllUsers", "Error " + t.message)
            }

        })
    }
}