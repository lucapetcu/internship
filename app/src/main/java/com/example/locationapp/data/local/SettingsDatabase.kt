package com.example.locationapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SettingsEntity::class], version = 1)
abstract class SettingsDatabase: RoomDatabase() {
    abstract fun settingsDao(): SettingsDao
}