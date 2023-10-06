package com.globa.balinasofttestapp.photos.internal.photos

import com.globa.balinasofttestapp.common.di.IoDispatcher
import com.globa.balinasofttestapp.network.api.ImagesNetworkApi
import com.globa.balinasofttestapp.network.api.model.ImageDtoIn
import com.globa.balinasofttestapp.photos.api.model.UploadPhoto
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ActivityRetainedScoped
internal class PhotosNetworkDataSource @Inject constructor(
    private val api: ImagesNetworkApi,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) {
    suspend fun removePhoto(token: String, id: Int) =
        withContext(coroutineDispatcher) {
            api.deleteImage(token = token, id = id)
        }

    suspend fun uploadPhoto(token: String, photo: UploadPhoto) = withContext(coroutineDispatcher) {
        api.postImage(
            token = token,
            imageDtoIn = ImageDtoIn(
                image = photo.base64Image,
                date = photo.date,
                latitude = photo.latitude,
                longitude = photo.longitude
            )
        )
    }
}