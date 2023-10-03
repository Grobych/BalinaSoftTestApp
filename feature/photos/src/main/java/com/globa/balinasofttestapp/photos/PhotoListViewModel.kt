package com.globa.balinasofttestapp.photos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.map
import com.globa.balinasofttestapp.login.api.AuthData
import com.globa.balinasofttestapp.login.api.LoginRepository
import com.globa.balinasofttestapp.photos.api.PhotosRepository
import com.globa.balinasofttestapp.photos.api.model.Photo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoListViewModel @Inject constructor(
    private val photosRepository: PhotosRepository,
    private val loginRepository: LoginRepository
): ViewModel() {
    private val _photosUiState = MutableStateFlow<PhotosUiState>(PhotosUiState.Loading)
    val photosUiState = _photosUiState.asStateFlow()

    init {
        viewModelScope.launch {
            loginRepository.getLoginStatus()
                .collect { authData ->
                    when (authData) {
                        is AuthData.Success -> {
                            if (authData.token.isNotEmpty()) {
                                _photosUiState.value = PhotosUiState.Done(
                                    photosRepository.getPhotos(authData.token).map { pagingData ->
                                        pagingData.map {
                                            Photo(
                                                id = it.id,
                                                date = it.date,
                                                url = it.url
                                            )
                                        }
                                    }
                                )
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