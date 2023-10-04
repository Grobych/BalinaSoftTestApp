package com.globa.balinasofttestappphotodetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globa.balinasofttestapp.comments.api.CommentsRepository
import com.globa.balinasofttestapp.comments.api.model.UploadComment
import com.globa.balinasofttestapp.login.api.AuthData
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
    private val commentsRepository: CommentsRepository,
    private val loginRepository: LoginRepository
): ViewModel() {
    private val _uiState = MutableStateFlow<PhotoDetailsUiState>(PhotoDetailsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _commentsUiState = MutableStateFlow<CommentsUiState>(CommentsUiState.Loading)
    val commentsUiState = _commentsUiState.asStateFlow()

    private val _commentTextFieldState = MutableStateFlow("")
    val commentTextFieldState = _commentTextFieldState.asStateFlow()

    private val photoId = savedStateHandle.get<Int>("photoId")
    private val _token = MutableStateFlow<String?>(null)

    init {
        getToken()
        loadPhoto()
        getComments()
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

    private fun getComments() {
        viewModelScope.launch {
            _token.collect { token ->
                if (token != null && photoId != null){
                    _commentsUiState.value = CommentsUiState.Done(
                        commentsRepository.getComments(token = token, imageId = photoId)
                    )
                }
            }
        }
    }

    fun addComment() {
        println(commentsUiState.value)
        viewModelScope.launch {
            if (_commentsUiState.value is CommentsUiState.Done) {
                val token = _token.value
                if (token != null && photoId != null) {
                    when(val response = commentsRepository.addComment(token,photoId, UploadComment(commentTextFieldState.value))) {
                        is com.globa.balinasofttestapp.comments.api.model.Response.Error -> _commentsUiState.value = CommentsUiState.Error(response.message)
                        is com.globa.balinasofttestapp.comments.api.model.Response.Success -> {
                            _commentTextFieldState.value = ""
                            getComments()
                        }
                    }
                }
            }
        }
    }

    private fun getToken() {
        viewModelScope.launch {
            loginRepository.getLoginStatus()
                .collect { authData ->
                    when (authData) {
                        is AuthData.Success -> {
                            if (authData.token.isNotEmpty()) _token.value = authData.token
                            else  _uiState.value = PhotoDetailsUiState.Error(message = "Not authorized: no token!")
                        }
                        is AuthData.Error -> _uiState.value = PhotoDetailsUiState.Error(message = "Not authorized: ${authData.message}!")
                    }
                }
        }
    }

    fun onCommentTextFieldChange(text: String) {
//        if (commentsUiState.value is CommentsUiState.Done)
            _commentTextFieldState.value = text
    }
}