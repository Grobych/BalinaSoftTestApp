package com.globa.balinasofttestapp.photos.internal

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.globa.balinasofttestapp.network.api.model.NetworkResponse
import com.globa.balinasofttestapp.photos.api.PhotosRepository
import com.globa.balinasofttestapp.photos.api.model.PhotoDetails
import com.globa.balinasofttestapp.photos.api.model.Response
import com.globa.balinasofttestapp.photos.api.model.UploadPhoto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PhotosRepositoryImpl @Inject constructor(
    private val photosNetworkDataSource: PhotosNetworkDataSource,
    private val photosLocalDataSource: PhotosLocalDataSource
): PhotosRepository {
    override suspend fun getPhotos(token: String): Flow<PagingData<PhotoDetails>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = {
            PhotosPagingSource(
                networkDataSource = photosNetworkDataSource,
                localDataSource = photosLocalDataSource,
                token = token
            )
        }
    ).flow

    override suspend fun getPhoto(token: String, id: Int): Response<PhotoDetails> {
        return photosNetworkDataSource.getPhotoById(token, id)
    }

    override suspend fun uploadPhoto(token: String, photo: UploadPhoto): Response<PhotoDetails> {
        return when (val response = photosNetworkDataSource.uploadPhoto(token, photo)) {
            is NetworkResponse.Error -> Response.Error(response.message)
            is NetworkResponse.Exception -> Response.Error(response.e.toString())
            is NetworkResponse.Success -> Response.Success(
                PhotoDetails(
                    id = response.data.body.id,
                    url = response.data.body.url,
                    date = response.data.body.date,
                    latitude = response.data.body.latitude,
                    longitude = response.data.body.longitude
                )
            )
        }
    }

    override suspend fun removePhoto(token: String, id: Int) {
        photosNetworkDataSource.removePhoto(token, id)
    }

}