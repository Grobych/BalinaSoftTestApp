package com.globa.balinasofttestapp.comments.internal

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.globa.balinasofttesrapp.database.api.PhotosDatabase
import com.globa.balinasofttesrapp.database.api.model.CommentDBModel
import com.globa.balinasofttesrapp.database.api.model.CommentsRemoteKey
import com.globa.balinasofttestapp.network.api.CommentsNetworkApi
import com.globa.balinasofttestapp.network.api.model.NetworkResponse
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
internal class CommentsRemoteMediator(
    private val token: String,
    private val imageId: Int,
    private val database: PhotosDatabase,
    private val api: CommentsNetworkApi
) : RemoteMediator<Int, CommentDBModel>() {
    private val commentsDao = database.commentsDao
    private val remoteKeyDao = database.commentsRemoteKeyDao
    private val remoteKeyLine = "discover_comment"
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CommentDBModel>
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
                        remoteKeyDao.getKeyByComment(remoteKeyLine)
                    } ?: return MediatorResult.Success(true)

                    if(remoteKey.nextPage == null) {
                        return MediatorResult.Success(true)
                    }
                    remoteKey.nextPage
                }
            }
            val response = api.getComments(
                token = token,
                id = imageId,
                page = page!!
            )
            when (response) {
                is NetworkResponse.Error -> MediatorResult.Error(Throwable(response.message))
                is NetworkResponse.Exception -> MediatorResult.Error(response.e)
                is NetworkResponse.Success -> {
                    database.withTransaction {
                        if (loadType == LoadType.REFRESH) {
                            commentsDao.clearAll()
                        }
                        val nextPage = if(response.data.body.isEmpty()) {
                            null
                        } else {
                            page.plus(1)
                        }
                        remoteKeyDao.insertKey(
                            CommentsRemoteKey(
                                id = remoteKeyLine,
                                nextPage = nextPage
                            )
                        )
                        commentsDao.addComments(response.data.body.map {
                            CommentDBModel(
                                id = it.id,
                                photoId = imageId,
                                date = it.date,
                                text = it.text
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