package com.globa.balinasofttestapp.photos.internal

import com.globa.balinasofttestapp.common.IoDispatcher
import com.globa.balinasofttestapp.network.api.ImagesNetworkApi
import com.globa.balinasofttestapp.network.api.model.NetworkResponse
import com.globa.balinasofttestapp.photos.api.model.Photo
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
    suspend fun getPhotos(token: String, page: Int): Response<List<Photo>> =
        withContext(coroutineDispatcher) {
            when (val result = api.getImages(token = token, page = page)) {
                is NetworkResponse.Success -> {
                    Response.Success(
                        result.data.body
                            .map {
                                Photo(
                                    id = it.id,
                                    url = it.url,
                                    date = it.date)
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

    suspend fun getPhotoById(token: String, id: Int): Response<Photo> =
        withContext(coroutineDispatcher){
            when (val response = api.getImage(token = token, id = id)) {
                is NetworkResponse.Success -> {
                    Response.Success(
                        data = Photo(
                            id = response.data.body.id,
                            date = response.data.body.date,
                            url = response.data.body.url
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
        //TODO: not implemented
    }
}