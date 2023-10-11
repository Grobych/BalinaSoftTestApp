package com.globa.balinasofttestapp.photos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.map
import com.globa.balinasofttestapp.login.api.AuthData
import com.globa.balinasofttestapp.login.api.LoginRepository
import com.globa.balinasofttestapp.photos.api.PhotosRepository
import com.globa.balinasofttestapp.photos.api.model.Photo
import com.globa.balinasofttestapp.photos.api.model.Response
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

    private val _deletePhotoUiState = MutableStateFlow<DeletePhotoUiState>(DeletePhotoUiState.NoRequest)
    val deletePhotoUiState = _deletePhotoUiState.asStateFlow()

    private val _token = MutableStateFlow<String?>(null)

    init {
        getToken()
        refresh()
    }

    private fun getToken() {
        viewModelScope.launch {
            loginRepository.getLoginStatus()
                .collect { authData ->
                    when (authData) {
                        is AuthData.Success -> {
                            if (authData.token.isNotEmpty()) {
                                _token.value = authData.token
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

    fun refresh() {
        viewModelScope.launch {
            _token.collect { token ->
                if (token != null){
                    _photosUiState.value = PhotosUiState.Done(
                        photosRepository.getPhotos(token).map { pagingData ->
                            pagingData.map {
                                Photo(
                                    id = it.id,
                                    date = it.date,
                                    url = it.url
                                )
                            }
                        }
                    )
                }
            }
        }
    }

    fun requestToRemovePhoto(id: Int) {
        _deletePhotoUiState.value = DeletePhotoUiState.RequestToRemove(id)
    }

    fun onDeleteRequestApproved() {
        val deleteState = deletePhotoUiState.value
        if (deleteState is DeletePhotoUiState.RequestToRemove) {
            removePhoto(deleteState.id)
            _deletePhotoUiState.value = DeletePhotoUiState.NoRequest
        }

    }

    fun onDeleteRequestDenied() {
        _deletePhotoUiState.value = DeletePhotoUiState.NoRequest
    }
    private fun removePhoto(id: Int) {
        viewModelScope.launch {
            val token = _token.value
            if (token != null) {
                when (val response = photosRepository.removePhoto(token = token, id = id)) {
                    is Response.Error -> _photosUiState.update { PhotosUiState.Error(response.message) }
                    is Response.Success -> refresh()
                }
            }
        }
    }
}