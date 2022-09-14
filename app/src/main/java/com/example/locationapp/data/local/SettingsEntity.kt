package com.example.locationapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SettingsEntity(val interval: Long,
                          val fastestInterval: Long,
                          @PrimaryKey val id: Int? = null)