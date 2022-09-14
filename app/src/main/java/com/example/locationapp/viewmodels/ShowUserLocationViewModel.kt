package com.example.locationapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.locationapp.domain.AppRepository
import com.example.locationapp.models.Coordinates
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShowUserLocationViewModel @Inject constructor(private val repo: AppRepository
): ViewModel() {
    private lateinit var coordinates: ArrayList<Coordinates>
    var liveCoordinates: LiveData<ArrayList<Coordinates>> = liveData {
        coordinates = repo.getLastCoordinates(repo.getUsername())
        emit(coordinates)
    }
}