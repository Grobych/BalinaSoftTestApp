package com.globa.balinasofttestapp.photos.api

import androidx.paging.PagingData
import com.globa.balinasofttestapp.photos.api.model.Photo
import com.globa.balinasofttestapp.photos.api.model.Response
import com.globa.balinasofttestapp.photos.api.model.UploadPhoto
import kotlinx.coroutines.flow.Flow

interface PhotosRepository {
    suspend fun getPhotos(token: String): Flow<PagingData<Photo>>
    suspend fun getPhoto(token: String, id: Int): Response<Photo>
    suspend fun uploadPhoto(token: String, photo: UploadPhoto)
    suspend fun removePhoto(token: String, id: Int)
}