package com.globa.balinasofttestappphotodetails

import com.globa.balinasofttestapp.photos.api.model.PhotoDetails

sealed class PhotoDetailsUiState {
    object Loading: PhotoDetailsUiState()
    data class Done(val photo: PhotoDetails): PhotoDetailsUiState()
    data class Error(val message: String): PhotoDetailsUiState()
}
