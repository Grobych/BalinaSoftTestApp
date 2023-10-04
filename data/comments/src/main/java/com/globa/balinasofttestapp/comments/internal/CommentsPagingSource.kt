package com.globa.balinasofttestapp.comments.internal

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.globa.balinasofttestapp.comments.api.model.Comment
import com.globa.balinasofttestapp.comments.api.model.Response

internal class CommentsPagingSource(
    private val networkDataSource: CommentsNetworkDataSource,
    private val token: String,
    private val imageId: Int
): PagingSource<Int, Comment>() {
    override fun getRefreshKey(state: PagingState<Int, Comment>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Comment> {
        return try {
            val page = params.key ?: 0
            when (val response = networkDataSource.getComments(token = token, page = page, imageId = imageId)) {
                is Response.Success -> {
                    LoadResult.Page(
                        data = response.data,
                        prevKey = if (page == 0) null else page.minus(1),
                        nextKey = if (response.data.isEmpty()) null else page.plus(1)
                    )
                }
                is Response.Error -> {
                    LoadResult.Error(
                        Throwable(message = response.message)
                    )
                }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}