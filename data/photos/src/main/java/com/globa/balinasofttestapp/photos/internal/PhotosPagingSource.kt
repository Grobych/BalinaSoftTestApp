package com.globa.balinasofttestapp.photos.internal

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.globa.balinasofttestapp.photos.api.model.PhotoDetails
import com.globa.balinasofttestapp.photos.api.model.Response

internal class PhotosPagingSource(
    private val networkDataSource: PhotosNetworkDataSource,
    private val localDataSource: PhotosLocalDataSource,
    private val token: String
): PagingSource<Int, PhotoDetails>() {
    override fun getRefreshKey(state: PagingState<Int, PhotoDetails>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoDetails> {
        return try {
            val page = params.key ?: 0
            when (val response = networkDataSource.getPhotos(token = token, page = page)) {
                is Response.Success -> {
                    localDataSource.savePhotos(response.data)
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