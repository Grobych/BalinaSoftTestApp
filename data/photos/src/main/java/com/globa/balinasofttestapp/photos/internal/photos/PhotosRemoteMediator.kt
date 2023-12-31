package com.globa.balinasofttestapp.photos.internal.photos

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.globa.balinasofttesrapp.database.api.PhotosDatabase
import com.globa.balinasofttesrapp.database.api.model.PhotoDBModel
import com.globa.balinasofttesrapp.database.api.model.PhotosRemoteKey
import com.globa.balinasofttestapp.network.api.ImagesNetworkApi
import com.globa.balinasofttestapp.network.api.model.NetworkResponse
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PhotosRemoteMediator(
    private val token: String,
    private val database: PhotosDatabase,
    private val api: ImagesNetworkApi
) : RemoteMediator<Int, PhotoDBModel>() {
    private val photosDao = database.photosDao
    private val remoteKeyDao = database.photosRemoteKeyDao
    private val remoteKeyLine = "discover_photo"
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PhotoDBModel>
    ): MediatorResult {
        return try {
            val page = when(loadType) {
                LoadType.REFRESH -> {
                    0
                }
                LoadType.PREPEND -> {
                    return MediatorResult.Success(true)
                }
                LoadType.APPEND -> {
                    val remoteKey = database.withTransaction {
                        remoteKeyDao.getKeyByPhoto(remoteKeyLine)
                    } ?: return MediatorResult.Success(true)

                    if(remoteKey.nextPage == null) {
                        return MediatorResult.Success(true)
                    }

                    remoteKey.nextPage
                }
            }
            val response = api.getImages(
                token = token,
                page = page?:0
            )
            when (response) {
                is NetworkResponse.Error -> MediatorResult.Error(Throwable(response.message))
                is NetworkResponse.Exception -> MediatorResult.Error(response.e)
                is NetworkResponse.Success -> {
                    database.withTransaction {
                        if (loadType == LoadType.REFRESH) {
                            photosDao.clearAll()
                        }
                        val nextPage = if(response.data.body.isEmpty()) {
                            null
                        } else {
                            page?.plus(1)
                        }
                        remoteKeyDao.insertKey(
                            PhotosRemoteKey(
                                id = remoteKeyLine,
                                nextPage = nextPage
                            )
                        )
                        photosDao.insertAll(response.data.body.map {
                            PhotoDBModel(
                                id = it.id,
                                date = it.date,
                                url = it.url,
                                latitude = it.latitude,
                                longitude = it.longitude
                            )
                        })
                    }
                    MediatorResult.Success(
                        endOfPaginationReached = response.data.body.isEmpty()
                    )
                }
            }
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}