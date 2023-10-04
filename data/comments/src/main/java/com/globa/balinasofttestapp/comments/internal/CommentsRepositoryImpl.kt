package com.globa.balinasofttestapp.comments.internal

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.globa.balinasofttestapp.comments.api.CommentsRepository
import com.globa.balinasofttestapp.comments.api.model.Comment
import com.globa.balinasofttestapp.comments.api.model.Response
import com.globa.balinasofttestapp.comments.api.model.UploadComment
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class CommentsRepositoryImpl @Inject constructor(
    private val networkDataSource: CommentsNetworkDataSource
): CommentsRepository {
    override suspend fun getComments(token: String, imageId: Int): Flow<PagingData<Comment>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = {
            CommentsPagingSource(
                networkDataSource = networkDataSource,
               token = token,
               imageId = imageId
         ) }).flow

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