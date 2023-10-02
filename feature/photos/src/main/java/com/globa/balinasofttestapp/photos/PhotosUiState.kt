package com.globa.balinasofttestapp.photos

import androidx.paging.PagingData
import com.globa.balinasofttestapp.photos.api.model.Photo
import kotlinx.coroutines.flow.Flow

sealed class PhotosUiState {
    object Loading: PhotosUiState()
    data class Done(val photos: Flow<PagingData<Photo>>): PhotosUiState()
    data class Error(val message: String): PhotosUiState()
}
