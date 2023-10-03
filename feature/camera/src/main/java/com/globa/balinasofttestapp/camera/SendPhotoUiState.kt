package com.globa.balinasofttestapp.camera

import android.graphics.Bitmap
import android.location.Location

sealed class SendPhotoUiState {
    object Creating: SendPhotoUiState()
    data class Processing(val stateMessage: String): SendPhotoUiState()
    data class ReadyToSend(val bitmap: Bitmap, val location: Location): SendPhotoUiState()
    object Sending: SendPhotoUiState()
    object Done: SendPhotoUiState()
    data class Error(val message: String): SendPhotoUiState()
}
