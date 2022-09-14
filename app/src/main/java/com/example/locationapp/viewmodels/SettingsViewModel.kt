package com.example.locationapp.viewmodels

import androidx.lifecycle.*
import com.example.locationapp.domain.SettingsModel
import com.example.locationapp.domain.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val repo: AppRepository): ViewModel() {

    lateinit var liveData: LiveData<SettingsModel>

    init {
        viewModelScope.launch {
            liveData = repo.getSettings()
        }
    }
}