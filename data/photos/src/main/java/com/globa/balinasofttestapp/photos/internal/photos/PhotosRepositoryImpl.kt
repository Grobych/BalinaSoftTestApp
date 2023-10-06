package com.globa.balinasofttestapp.photos.internal.photos

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.globa.balinasofttesrapp.database.api.PhotosDatabase
import com.globa.balinasofttestapp.network.api.ImagesNetworkApi
import com.globa.balinasofttestapp.network.api.model.NetworkResponse
import com.globa.balinasofttestapp.photos.api.PhotosRepository
import com.globa.balinasofttestapp.photos.api.model.PhotoDetails
import com.globa.balinasofttestapp.photos.api.model.Response
import com.globa.balinasofttestapp.photos.api.model.UploadPhoto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PhotosRepositoryImpl @Inject constructor(
    private val photosNetworkDataSource: PhotosNetworkDataSource,
    private val photosLocalDataSource: PhotosLocalDataSource,
    private val api: ImagesNetworkApi,
    private val database: PhotosDatabase
): PhotosRepository {
    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getPhotos(token: String): Flow<PagingData<PhotoDetails>> = Pager(
        config = PagingConfig(pageSize = 20),
        remoteMediator = PhotosRemoteMediator(
            token = token,
            api = api,
            database = database
        ),
        pagingSourceFactory = {
            database.photosDao.getPhotos()
        }
    ).flow.map { pagingData -> pagingData.map { it.asDomainModel() } }.flowOn(Dispatchers.IO)

    override suspend fun getPhoto(id: Int): Flow<Response<PhotoDetails>> {
        return photosLocalDataSource
            .getPhoto(id)
            .map { Response.Success(data = it) }
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

    override suspend fun removePhoto(token: String, id: Int): Response<Boolean> {
        val networkResponse = photosNetworkDataSource.removePhoto(token, id)
        val databaseResponse = photosLocalDataSource.removePhoto(id)
        return if (networkResponse is NetworkResponse.Success && databaseResponse != 0)
            Response.Success(true)
        else Response.Error(message = "Delete error")
    }

}