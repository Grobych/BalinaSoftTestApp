package com.globa.balinasofttestapp.location.internal

import com.globa.balinasofttestapp.location.api.LocationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class LocationRepositoryImpl @Inject constructor(
    private val locationDataSource: LocationDataSource
): LocationRepository {
    override suspend fun getLocation() = locationDataSource.getCurrentLocation()
}