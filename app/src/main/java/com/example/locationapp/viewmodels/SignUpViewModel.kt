package com.example.locationapp.viewmodels

import androidx.lifecycle.ViewModel
import com.example.locationapp.domain.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val repo: AppRepository): ViewModel() {
    fun postUserToServer(email: String, name: String, token: String) {
        repo.postUserToServer(email, name, token)
    }
    fun sendDeviceTokenToServer(deviceToken: String, userToken: String) {
        repo.sendDeviceToken(deviceToken, userToken)
    }
}