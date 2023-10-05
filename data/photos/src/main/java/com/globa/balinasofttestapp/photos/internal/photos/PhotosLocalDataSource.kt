package com.globa.balinasofttestapp.photos.internal.photos

import com.globa.balinasofttesrapp.database.api.PhotosDatabase
import com.globa.balinasofttesrapp.database.api.model.PhotoDBModel
import com.globa.balinasofttestapp.common.di.IoDispatcher
import com.globa.balinasofttestapp.photos.api.model.PhotoDetails
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PhotosLocalDataSource @Inject constructor(
    private val database: PhotosDatabase,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
){
    suspend fun savePhotos(photos: List<PhotoDetails>) = withContext(coroutineDispatcher) {
        database.photosDao.insertAll(photos.map { it.asDBModel() })
    }

    suspend fun savePhoto(photo: PhotoDetails) = withContext(coroutineDispatcher) {
        database.photosDao.insertPhoto(photo.asDBModel())
    }

    suspend fun getPhoto(id: Int): Flow<PhotoDetails> = withContext(coroutineDispatcher) {
        database.photosDao.getPhotoById(id).map { it.asDomainModel() }
    }

    suspend fun removePhoto(id: Int): Int = withContext(coroutineDispatcher) {
        database.photosDao.removePhoto(PhotoDBModel(id = id))
    }
}

fun PhotoDetails.asDBModel() : PhotoDBModel =
    PhotoDBModel(
        id = this.id,
        url = this.url,
        date = this.date,
        latitude = this.latitude,
        longitude = this.longitude
    )

fun PhotoDBModel.asDomainModel() : PhotoDetails =
    PhotoDetails(
        id = this.id,
        url = this.url,
        date = this.date,
        latitude = this.latitude,
        longitude = this.longitude
    )