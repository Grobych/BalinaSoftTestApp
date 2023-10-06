package com.globa.balinasofttestapp.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globa.balinasofttestapp.location.api.LocationRepository
import com.globa.balinasofttestapp.location.api.LocationResponse
import com.globa.balinasofttestapp.login.api.AuthData
import com.globa.balinasofttestapp.login.api.LoginRepository
import com.globa.balinasofttestapp.photos.api.PhotosRepository
import com.globa.balinasofttestapp.photos.api.model.Response
import com.globa.balinasofttestapp.photos.api.model.UploadPhoto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SendPhotoViewModel @Inject constructor(
    private val photosRepository: PhotosRepository,
    private val locationRepository: LocationRepository,
    private val loginRepository: LoginRepository
): ViewModel() {
    private val _uiState = MutableStateFlow<SendPhotoUiState>(SendPhotoUiState.Creating)
    val uiState = _uiState.asStateFlow()

    private val _imageToSend = MutableStateFlow<UploadPhoto?>(null)
    private val _token = MutableStateFlow<String?>(null)

    init {
        viewModelScope.launch {
            loginRepository.getLoginStatus()
                .onEach { authData ->
                    println(authData)
                    if (authData is AuthData.Success) {
                        _token.value = authData.token
                    }
                }
                .collect()
        }
    }

    fun onPhotoCaptured(byteArray: ByteArray) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                _uiState.update { SendPhotoUiState.Processing("Processing image...") }
                val bitmap = BitmapFormatter.createBitmap(byteArray)
                _uiState.update { SendPhotoUiState.Processing("Compressing image...") }
                val base64 = BitmapFormatter.get64Image(byteArray)
                _uiState.update { SendPhotoUiState.Processing("Getting location data...") }
                when (val response = locationRepository.getLocation()) {
                    is LocationResponse.Error -> _uiState.update { SendPhotoUiState.Error("Getting location error: ${response.message}") }
                    is LocationResponse.Success -> _uiState.update {
                        _imageToSend.value = UploadPhoto(
                            base64Image = base64,
                            date = System.currentTimeMillis() / 1000L,
                            latitude = response.location.latitude,
                            longitude = response.location.longitude
                        )
                        SendPhotoUiState.ReadyToSend(bitmap,response.location)
                    }
                }
            }
        }
    }
    fun onSendButtonClick() {
        if (uiState.value is SendPhotoUiState.ReadyToSend) {
            val image = _imageToSend.value
            if (image != null) {
                viewModelScope.launch {
                    _uiState.update { SendPhotoUiState.Processing("Requesting token") }
                    val token = _token.value
                    if (token != null) {
                        _uiState.update { SendPhotoUiState.Sending }
                        val response = photosRepository.uploadPhoto(
                            token = token,
                            photo = image
                        )
                        when (response) {
                            is Response.Error -> _uiState.update { SendPhotoUiState.Error(response.message) }
                            is Response.Success -> _uiState.update { SendPhotoUiState.Done }
                        }
                    }
                }
            }
        }
    }
}