package com.example.locationapp.data.mapper

import com.example.locationapp.data.local.SettingsEntity
import com.example.locationapp.domain.SettingsModel

fun SettingsEntity.toSettingsModel(): SettingsModel {
    return SettingsModel(interval = interval, fastestInterval = fastestInterval)
}

fun SettingsModel.toSettingsEntity(): SettingsEntity {
    return SettingsEntity(interval = interval, fastestInterval = fastestInterval)
}