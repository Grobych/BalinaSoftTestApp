package com.globa.balinasofttestapp.photos.internal.photos

import com.globa.balinasofttestapp.common.di.IoDispatcher
import com.globa.balinasofttestapp.network.api.ImagesNetworkApi
import com.globa.balinasofttestapp.network.api.model.ImageDtoIn
import com.globa.balinasofttestapp.network.api.model.NetworkResponse
import com.globa.balinasofttestapp.photos.api.model.PhotoDetails
import com.globa.balinasofttestapp.photos.api.model.Response
import com.globa.balinasofttestapp.photos.api.model.UploadPhoto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PhotosNetworkDataSource @Inject constructor(
    private val api: ImagesNetworkApi,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) {
    suspend fun getPhotos(token: String, page: Int): Response<List<PhotoDetails>> =
        withContext(coroutineDispatcher) {
            when (val result = api.getImages(token = token, page = page)) {
                is NetworkResponse.Success -> {
                    Response.Success(
                        result.data.body
                            .map {
                                PhotoDetails(
                                    id = it.id,
                                    url = it.url,
                                    date = it.date,
                                    latitude = it.latitude,
                                    longitude = it.longitude
                                )
                            }
                    )
                }
                is NetworkResponse.Error -> {
                    Response.Error(message = result.message)
                }
                is NetworkResponse.Exception -> {
                    Response.Error(message = "Exception: ${result.e.message?: "Unknown error"}")
                }
            }
    }

    suspend fun getPhotoById(token: String, id: Int): Response<PhotoDetails> =
        withContext(coroutineDispatcher){
            when (val response = api.getImage(token = token, id = id)) {
                is NetworkResponse.Success -> {
                    Response.Success(
                        data = PhotoDetails(
                            id = response.data.body.id,
                            date = response.data.body.date,
                            url = response.data.body.url,
                            latitude = response.data.body.latitude,
                            longitude = response.data.body.longitude
                        )
                    )
                }
                is NetworkResponse.Error -> {
                    Response.Error(
                        message = response.message
                    )
                }
                is NetworkResponse.Exception -> {
                    Response.Error(
                        message = response.e.message?: "Unknown message"
                    )
                }
            }
        }


    suspend fun removePhoto(token: String, id: Int) =
        withContext(coroutineDispatcher) {
            api.deleteImage(token = token, id = id) //TODO: ?check result?
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