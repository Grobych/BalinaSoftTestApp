package com.globa.balinasofttestapp.comments.internal

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.globa.balinasofttesrapp.database.api.PhotosDatabase
import com.globa.balinasofttesrapp.database.api.model.CommentDBModel
import com.globa.balinasofttestapp.comments.api.CommentsRepository
import com.globa.balinasofttestapp.comments.api.model.Comment
import com.globa.balinasofttestapp.comments.api.model.Response
import com.globa.balinasofttestapp.comments.api.model.UploadComment
import com.globa.balinasofttestapp.network.api.CommentsNetworkApi
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ViewModelScoped
internal class CommentsRepositoryImpl @Inject constructor(
    private val networkDataSource: CommentsNetworkDataSource,
    private val api: CommentsNetworkApi,
    private val database: PhotosDatabase
): CommentsRepository {
    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getComments(token: String, imageId: Int): Flow<PagingData<Comment>> = Pager(
        config = PagingConfig(pageSize = 20),
        remoteMediator = CommentsRemoteMediator(
            token = token,
            imageId = imageId,
            api = api,
            database = database
        ),
        pagingSourceFactory = {
            database.commentsDao.getComments(imageId = imageId)
        }
    ).flow.map { pagingData -> pagingData.map { it.asDomainModel() } }.flowOn(Dispatchers.IO)

    override suspend fun addComment(token: String, imageId: Int, comment: UploadComment): Response<Comment> {
        return when (val result = networkDataSource.sendComment(token = token, imageId = imageId, comment = comment)) {
            is Response.Error -> Response.Error(message = result.message)
            is Response.Success -> Response.Success(data = result.data)
        }
    }

    override suspend fun removeComment(
        token: String,
        imageId: Int,
        commentId: Int
    ): Response<Comment> {
        return when (val result = networkDataSource.deleteComment(token = token, imageId = imageId, commentId = commentId)) {
            is Response.Error -> Response.Error(message = result.message)
            is Response.Success -> Response.Success(data = result.data)
        }
    }
}

fun CommentDBModel.asDomainModel(): Comment =
    Comment(
        id = this.id,
        date = this.date,
        text = this.text
    )