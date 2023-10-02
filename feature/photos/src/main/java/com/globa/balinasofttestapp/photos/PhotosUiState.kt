package com.globa.balinasofttestapp.photos

import com.globa.balinasofttestapp.photos.api.model.Photo

sealed class PhotosUiState {
    object Loading: PhotosUiState()
    data class Success(val photos: List<Photo>): PhotosUiState()
    data class Error(val message: String)
}
