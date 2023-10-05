package com.globa.balinasofttestappphotodetails

import androidx.paging.PagingData
import com.globa.balinasofttestapp.comments.api.model.Comment
import com.globa.balinasofttestapp.photos.api.model.PhotoDetails
import kotlinx.coroutines.flow.Flow

sealed class PhotoDetailsUiState {
    object Loading: PhotoDetailsUiState()
    data class Done(val photo: PhotoDetails): PhotoDetailsUiState()
    data class Error(val message: String): PhotoDetailsUiState()
}
sealed class CommentsUiState {
    object Loading: CommentsUiState()
    data class Done(val comments: Flow<PagingData<Comment>>, val showDeleteDialog: Boolean = false): CommentsUiState()
    data class Error(val message: String): CommentsUiState()
}
