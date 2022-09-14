package com.example.locationapp.di

import com.example.locationapp.data.repository.AppRepoImpl
import com.example.locationapp.domain.AppRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsModule {
    @Binds
    @Singleton
    abstract fun bindSettingsRepo(settingsRepoImpl: AppRepoImpl): AppRepository
}