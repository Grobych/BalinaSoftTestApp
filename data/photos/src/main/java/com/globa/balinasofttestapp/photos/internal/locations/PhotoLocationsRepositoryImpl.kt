package com.globa.balinasofttestapp.photos.internal.locations

import com.globa.balinasofttestapp.photos.api.PhotoLocationsRepository
import com.globa.balinasofttestapp.photos.api.model.PhotoLocation
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
internal class PhotoLocationsRepositoryImpl @Inject constructor(
    private val dataSource: PhotoLocationsLocalDataSource
): PhotoLocationsRepository {
    override suspend fun getPhotoLocations(): Flow<List<PhotoLocation>> = dataSource.getPhotoLocations()
}