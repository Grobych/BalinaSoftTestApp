package com.globa.balinasofttestapp.photos.api

import com.globa.balinasofttestapp.photos.api.model.PhotoLocation
import kotlinx.coroutines.flow.Flow

// TODO: move to separate module?
interface PhotoLocationsRepository {
    suspend fun getPhotoLocations(): Flow<List<PhotoLocation>>
}