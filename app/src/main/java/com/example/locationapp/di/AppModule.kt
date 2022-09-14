package com.example.locationapp.di

import android.app.Application
import androidx.room.Room
import com.example.locationapp.data.local.SettingsDatabase
import com.example.locationapp.network.LocationApi
import com.example.locationapp.network.SettingsApi
import com.example.locationapp.network.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideSettingsDatabase(app: Application): SettingsDatabase {
        return Room.databaseBuilder(app, SettingsDatabase::class.java, "settingsdb").build()
    }

    @Provides
    @Singleton
    fun provideRetrofitLocation(): LocationApi {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LocationApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofitSettings(): SettingsApi {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SettingsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofitUser(): UserApi {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApi::class.java)
    }
}