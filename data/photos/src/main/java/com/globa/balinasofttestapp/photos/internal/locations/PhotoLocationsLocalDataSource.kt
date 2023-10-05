package com.globa.balinasofttestapp.photos.internal.locations

import com.globa.balinasofttesrapp.database.api.PhotosDatabase
import com.globa.balinasofttesrapp.database.api.model.PhotoLocationDBModel
import com.globa.balinasofttestapp.common.di.IoDispatcher
import com.globa.balinasofttestapp.photos.api.model.PhotoLocation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class PhotoLocationsLocalDataSource @Inject constructor(
    private val database: PhotosDatabase,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) {
    suspend fun getPhotoLocations() = withContext(coroutineDispatcher) {
        database.photosDao.getPhotoLocations().map { list -> list.map { it.asDomainModel() } }
    }
}

fun PhotoLocationDBModel.asDomainModel() =
    PhotoLocation(
        id = this.id,
        latitude = this.latitude,
        longitude = this.longitude
    )