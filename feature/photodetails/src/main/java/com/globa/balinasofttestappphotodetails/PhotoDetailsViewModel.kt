package com.globa.balinasofttestappphotodetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globa.balinasofttestapp.login.api.LoginRepository
import com.globa.balinasofttestapp.photos.api.PhotosRepository
import com.globa.balinasofttestapp.photos.api.model.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val photosRepository: PhotosRepository,
    private val loginRepository: LoginRepository
): ViewModel() {
    private val _uiState = MutableStateFlow<PhotoDetailsUiState>(PhotoDetailsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val photoId = savedStateHandle.get<Int>("photoId")

    init {
        loadPhoto()
    }

    private fun loadPhoto() {
        viewModelScope.launch {
            if (photoId != null) {
                photosRepository.getPhoto(photoId)
                    .catch { _uiState.value = PhotoDetailsUiState.Error(it.toString()) }
                    .collect {
                        when (val response = it) {
                            is Response.Error -> _uiState.value = PhotoDetailsUiState.Error(it.toString())
                            is Response.Success -> _uiState.value = PhotoDetailsUiState.Done(photo = response.data)
                        }
                    }
            } else {
                _uiState.update { PhotoDetailsUiState.Error("Incorrect Id!") }
            }
        }
    }
}