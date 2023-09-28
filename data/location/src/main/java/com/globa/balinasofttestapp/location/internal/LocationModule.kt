package com.globa.balinasofttestapp.location.internal

import android.content.Context
import com.globa.balinasofttestapp.location.api.LocationRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class LocationModule {
    @Provides
    fun provideFusedLocationClient(context: Context): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
}

@Module
@InstallIn(SingletonComponent::class)
interface LocationRepositoryBinding {
    @Binds
    fun bindLocationRepository(repositoryImpl: LocationRepositoryImpl): LocationRepository
}