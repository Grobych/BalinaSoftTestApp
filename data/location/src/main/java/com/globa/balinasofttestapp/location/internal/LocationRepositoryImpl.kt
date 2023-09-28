package com.globa.balinasofttestapp.location.internal

import com.globa.balinasofttestapp.location.api.LocationRepository
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val locationDataSource: LocationDataSource
): LocationRepository {
    override suspend fun getLocation() = locationDataSource.getCurrentLocation()
}