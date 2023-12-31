package com.globa.balinasofttestapp.location.internal

import android.content.Context
import com.globa.balinasofttestapp.location.api.LocationRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class LocationModule {
    @Provides
    @Singleton
    fun provideFusedLocationClient(@ApplicationContext context: Context): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
}

@Module
@InstallIn(SingletonComponent::class)
internal interface LocationRepositoryBinding {
    @Binds
    fun bindLocationRepository(repositoryImpl: LocationRepositoryImpl): LocationRepository
}