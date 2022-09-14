package com.example.locationapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.locationapp.domain.AppRepository
import com.example.locationapp.models.User
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(private val repo: AppRepository): ViewModel() {
    lateinit var userList: ArrayList<User>
    var liveUserList: LiveData<ArrayList<User>> = liveData {
        userList = repo.getAllUsers(FirebaseAuth.getInstance().currentUser!!.uid)
        emit(userList)
    }
}