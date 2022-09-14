package com.example.locationapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.locationapp.adapters.UserAdapter
import com.example.locationapp.databinding.ActivityUserListBinding
import com.example.locationapp.models.UserListResponse
import com.example.locationapp.network.UserApi
import com.example.locationapp.viewmodels.UserListViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

@AndroidEntryPoint
class UserListActivity : AppCompatActivity() {
    private var binding: ActivityUserListBinding? = null
    private val viewModel: UserListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        viewModel.liveUserList.observe(this) {
            val adapter = UserAdapter(this, applicationContext, it)
            binding?.rvUsersList?.adapter = adapter
            binding?.rvUsersList?.layoutManager = LinearLayoutManager(this)
        }
    }
}