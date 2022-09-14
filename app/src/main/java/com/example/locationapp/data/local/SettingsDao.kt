package com.example.locationapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SettingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSettings(settings: SettingsEntity)

    @Query("DELETE FROM settingsentity")
    fun deleteSettings()

    @Query("SELECT * FROM settingsentity LIMIT 1")
    fun getSettings(): LiveData<SettingsEntity>
}