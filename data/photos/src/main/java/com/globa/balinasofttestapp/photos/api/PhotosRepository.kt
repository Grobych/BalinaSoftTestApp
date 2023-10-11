package com.globa.balinasofttestapp.photos.api

import androidx.paging.PagingData
import com.globa.balinasofttestapp.photos.api.model.PhotoDetails
import com.globa.balinasofttestapp.photos.api.model.Response
import com.globa.balinasofttestapp.photos.api.model.UploadPhoto
import kotlinx.coroutines.flow.Flow

interface PhotosRepository {
    suspend fun getPhotos(token: String): Flow<PagingData<PhotoDetails>>
    suspend fun getPhoto(id: Int): Flow<Response<PhotoDetails>>
    suspend fun uploadPhoto(token: String, photo: UploadPhoto): Response<PhotoDetails>
    suspend fun removePhoto(token: String, id: Int): Response<Boolean>
    suspend fun removeAllLocalPhotos()
}