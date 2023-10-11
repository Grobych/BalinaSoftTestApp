package com.globa.balinasofttestapp.photos.internal.photos

import com.globa.balinasofttesrapp.database.api.PhotosDatabase
import com.globa.balinasofttesrapp.database.api.model.PhotoDBModel
import com.globa.balinasofttestapp.common.di.IoDispatcher
import com.globa.balinasofttestapp.photos.api.model.PhotoDetails
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ActivityRetainedScoped
class PhotosLocalDataSource @Inject constructor(
    private val database: PhotosDatabase,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
){
    suspend fun getPhoto(id: Int): Flow<PhotoDetails> = withContext(coroutineDispatcher) {
        database.photosDao.getPhotoById(id).map { it.asDomainModel() }
    }

    suspend fun removePhoto(id: Int): Int = withContext(coroutineDispatcher) {
        database.photosDao.removePhoto(PhotoDBModel(id = id))
    }

    suspend fun addPhoto(photoDBModel: PhotoDBModel) = withContext(coroutineDispatcher) {
        database.photosDao.insertPhoto(photoDBModel)
    }

    suspend fun removeAll() = withContext(coroutineDispatcher) {
        database.photosDao.clearAll()
    }
}

fun PhotoDBModel.asDomainModel() : PhotoDetails =
    PhotoDetails(
        id = this.id,
        url = this.url,
        date = this.date,
        latitude = this.latitude,
        longitude = this.longitude
    )