package com.globa.balinasofttestapp.photos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globa.balinasofttestapp.camera.BitmapFormatter
import com.globa.balinasofttestapp.login.api.AuthData
import com.globa.balinasofttestapp.login.api.LoginRepository
import com.globa.balinasofttestapp.photos.api.PhotosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PhotoListViewModel @Inject constructor(
    private val photosRepository: PhotosRepository,
    private val loginRepository: LoginRepository
): ViewModel() {
    fun uploadPhoto(bytes: ByteArray) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                val base64 = BitmapFormatter.get64Image(bytes)

                println(base64.length)
            }
        }
    }

    private val _photosUiState = MutableStateFlow<PhotosUiState>(PhotosUiState.Loading)
    val photosUiState = _photosUiState.asStateFlow()

    init {
        viewModelScope.launch {
            loginRepository.getLoginStatus()
                .collect { authData ->
                    when (authData) {
                        is AuthData.Success -> {
                            if (authData.token.isNotEmpty()) {
                                _photosUiState.value = PhotosUiState.Done(photosRepository.getPhotos(authData.token))
                            } else {
                                _photosUiState.update {
                                    PhotosUiState.Error(message = "Not authorized: no token!")
                                }
                            }
                        }
                        is AuthData.Error -> {
                            _photosUiState.update {
                                PhotosUiState.Error(message = "Not authorized: ${authData.message}!")
                            }
                        }
                    }
                }
        }
    }
}