package com.globa.balinasofttestapp.comments.internal

import com.globa.balinasofttestapp.comments.api.model.Comment
import com.globa.balinasofttestapp.comments.api.model.Response
import com.globa.balinasofttestapp.comments.api.model.UploadComment
import com.globa.balinasofttestapp.common.di.IoDispatcher
import com.globa.balinasofttestapp.network.api.CommentsNetworkApi
import com.globa.balinasofttestapp.network.api.model.CommentPost
import com.globa.balinasofttestapp.network.api.model.NetworkResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class CommentsNetworkDataSource @Inject constructor(
    private val api: CommentsNetworkApi,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) {
    suspend fun getComments(token: String, imageId: Int, page: Int): Response<List<Comment>> =
        withContext(coroutineDispatcher) {
            when (val result = api.getComments(token = token, id = imageId, page = page)) {
                is NetworkResponse.Success -> {
                    Response.Success(result.data.body.map { it.asDomainModel() })
                }
                is NetworkResponse.Error -> {
                    Response.Error(message = result.message)
                }
                is NetworkResponse.Exception -> {
                    Response.Error(message = "Exception: ${result.e.message?: "Unknown error"}")
                }
            }
        }

    suspend fun sendComment(token: String, imageId: Int, comment: UploadComment): Response<Comment> =
        withContext(coroutineDispatcher) {
            when (val result = api.postComment(token = token, id = imageId, comment = comment.asNetworkModel())) {
                is NetworkResponse.Error -> Response.Error(result.message)
                is NetworkResponse.Exception -> Response.Error(result.e.toString())
                is NetworkResponse.Success -> Response.Success(result.data.body.asDomainModel())
            }
        }

    suspend fun deleteComment(token: String, imageId: Int, commentId: Int): Response<Comment> =
        withContext(coroutineDispatcher) {
            when (val result = api.deleteComment(token = token, imageId = imageId, commentId = commentId)) {
                is NetworkResponse.Error -> Response.Error(result.message)
                is NetworkResponse.Exception -> Response.Error(result.e.toString())
                is NetworkResponse.Success -> Response.Success(result.data.body.asDomainModel())
            }
        }
}

fun UploadComment.asNetworkModel(): CommentPost = CommentPost(this.text)

fun com.globa.balinasofttestapp.network.api.model.Comment.asDomainModel(): Comment = Comment(
    id = this.id,
    date = this.date,
    text = this.text
)