package com.globa.balinasofttestappphotodetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.globa.balinasofttestapp.common.ui.composable.BackHeader
import com.globa.balinasofttestapp.common.ui.composable.LoadingAnimation
import com.globa.balinasofttestapp.common.util.DateFormatter
import com.globa.balinasofttestapp.photos.api.model.PhotoDetails

@Composable
fun PhotoDetailScreen(
    onBackButtonClick: () -> Unit,
    viewModel: PhotoDetailsViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        BackHeader(
            onBackButtonClick = onBackButtonClick
        )
        when (val state = uiState.value) {
            is PhotoDetailsUiState.Done -> DonePhotoDetailsScreen(photo = state.photo)
            is PhotoDetailsUiState.Error -> ErrorPhotoDetailsScreen(errorMessage = state.message)
            PhotoDetailsUiState.Loading -> LoadingAnimation(modifier = Modifier.fillMaxSize())
        }
    }



}

@Composable
fun DonePhotoDetailsScreen(
    modifier: Modifier = Modifier,
    photo: PhotoDetails
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxWidth(),
            model = photo.url,
            contentDescription = "Photo ${photo.id}"
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background),
            text = DateFormatter.getExtendDate(photo.date),
            textAlign = TextAlign.Center
        )
        CommentsScreen()
    }
}

@Composable
fun CommentsScreen() {
    Text(text = "Comments")
}


@Composable
private fun ErrorPhotoDetailsScreen(
    modifier: Modifier = Modifier,
    errorMessage: String
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(bottom = 10.dp),
            text = errorMessage
        )
    }
}
