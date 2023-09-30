package com.globa.balinasofttestapp.photos.internal

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.globa.balinasofttestapp.photos.api.PhotosRepository
import com.globa.balinasofttestapp.photos.api.model.Photo
import com.globa.balinasofttestapp.photos.api.model.Response
import com.globa.balinasofttestapp.photos.api.model.UploadPhoto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PhotosRepositoryImpl @Inject constructor(
    private val photosNetworkDataSource: PhotosNetworkDataSource
): PhotosRepository {
    override suspend fun getPhotos(token: String): Flow<PagingData<Photo>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = {
            PhotosPagingSource(
                dataSource = photosNetworkDataSource,
                token = token
            )
        }
    ).flow

    override suspend fun getPhoto(token: String, id: Int): Response<Photo> {
        return photosNetworkDataSource.getPhotoById(token, id)
    }

    override suspend fun uploadPhoto(token: String, photo: UploadPhoto) {
        photosNetworkDataSource.uploadPhoto(token, photo)
    }

    override suspend fun removePhoto(token: String, id: Int) {
        photosNetworkDataSource.removePhoto(token, id)
    }

}