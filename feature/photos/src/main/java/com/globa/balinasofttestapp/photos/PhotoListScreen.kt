package com.globa.balinasofttestapp.photos

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import com.globa.balinasofttestapp.camera.CreatePhotoFloatingButton
import com.globa.balinasofttestapp.common.ui.composable.LoadingAnimation
import com.globa.balinasofttestapp.common.ui.composable.MenuHeader
import com.globa.balinasofttestapp.common.ui.theme.BalinaSoftTestAppTheme
import com.globa.balinasofttestapp.common.ui.theme.Paddings
import com.globa.balinasofttestapp.common.ui.theme.imageListSize
import com.globa.balinasofttestapp.common.util.DateFormatter
import com.globa.balinasofttestapp.photos.api.model.Photo
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
fun PhotoListScreen(
    drawerState: DrawerState,
    viewModel: PhotoListViewModel = hiltViewModel(),
    onPhotoClick: (Int) -> Unit,
    navigateToCamera: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val uiState = viewModel.photosUiState.collectAsState()
    val deletePhotoUiState = viewModel.deletePhotoUiState.collectAsState()

    if (deletePhotoUiState.value is DeletePhotoUiState.RequestToRemove) {
        AlertDialog(
            text = { Text(text = "Delete this photo?")},
            dismissButton = { Button(onClick = { viewModel.onDeleteRequestDenied() }) {
                Text(text = "Cancel")
            } },
            onDismissRequest = { viewModel.onDeleteRequestDenied() },
            confirmButton = { Button(onClick = { viewModel.onDeleteRequestApproved() }) {
                Text(text = "Ok")
            } }
        )
    }

    val requestToRemove = fun(id: Int) {
        viewModel.requestToRemovePhoto(id)
    }

    Scaffold(
        topBar = {
            MenuHeader {
                coroutineScope.launch {
                    drawerState.open()
                }
            }
        },
        floatingActionButton = {
            CreatePhotoFloatingButton(navigateToCameraScreen = navigateToCamera)
        }
    ) {
        when (val state = uiState.value) {
            is PhotosUiState.Loading -> {
                LoadingAnimation(modifier = Modifier.padding(it))
            }
            is PhotosUiState.Error -> {
                ErrorPhotoListScreen(
                    modifier = Modifier.padding(it),
                    errorMessage = state.message
                )
            }
            is PhotosUiState.Done -> {
                val photos = state.photos.collectAsLazyPagingItems()
                DonePhotoListScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it),
                    photos = photos,
                    onPhotoClick = onPhotoClick,
                    onPhotoLongClick = requestToRemove
                )
            }
        }
    }
}

@Composable
private fun ErrorPhotoListScreen(
    modifier: Modifier = Modifier,
    errorMessage: String
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = errorMessage
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DonePhotoListScreen(
    modifier: Modifier = Modifier,
    photos: LazyPagingItems<Photo>,
    onPhotoClick: (Int) -> Unit,
    onPhotoLongClick: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier.padding(Paddings.medium)
    ) {
        items(photos.itemCount) { index ->
            val photo = photos[index]
            photo?.let {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    SubcomposeAsyncImage(
                        modifier = Modifier
                            .size(100.dp)
                            .padding(start = Paddings.medium, end = Paddings.medium, top = Paddings.medium)
                            .combinedClickable(
                                onClick = { onPhotoClick(it.id) },
                                onLongClick = { onPhotoLongClick(it.id) }
                            )
                            .clip(MaterialTheme.shapes.medium),
                        model = it.url,
                        loading = { LoadingAnimation() },
                        error = {
                            Image(
                                painter = painterResource(id = com.globa.balinasofttestapp.common.R.drawable.ic_broken),
                                contentDescription = "Image broken or not found"
                            )
                        },
                        contentDescription = "Photo ${it.id}"
                    )
                    Text(text = DateFormatter.getSimpleDate(it.date))
                }
            }
        }
        when(val state = photos.loadState.refresh) {
            is LoadState.Loading -> item {
                LoadingAnimation(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(imageListSize))
            }
            is LoadState.Error -> item {
                ErrorPhotoListScreen(
                    modifier = Modifier.fillMaxSize(),
                    errorMessage = state.error.localizedMessage!!
                )
            }
            is LoadState.NotLoading -> item {

            }
        }
        when(val state = photos.loadState.append) {
            is LoadState.Loading -> item {
                LoadingAnimation(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(imageListSize))
            }
            is LoadState.Error -> item {
                ErrorPhotoListScreen(
                    modifier = Modifier.fillMaxSize(),
                    errorMessage = state.error.localizedMessage!!
                )
            }
            is LoadState.NotLoading -> item {

            }
        }
    }

}

@Preview
@Composable
fun DonePhotoListScreenPreview() {
    BalinaSoftTestAppTheme {
        val photos = flowOf(
            PagingData.from(
                listOf(
                    Photo(
                        id = 1,
                        url = "https://placebear.com/g/200/200",
                        date = 1262307723,
                    ),
                    Photo(
                        id = 2,
                        url = "https://placebear.com/g/200/200",
                        date = 1262307723,
                    ),
                    Photo(
                        id = 3,
                        url = "https://placebear.com/g/200/200",
                        date = 1262307723,
                    ),
                    Photo(
                        id = 4,
                        url = "https://placebear.com/g/200/200",
                        date = 1262307723,
                    ),
                    Photo(
                        id = 5,
                        url = "https://placebear.com/g/200/200",
                        date = 1262307723,
                    )
                )
            )
        ).collectAsLazyPagingItems()
        DonePhotoListScreen(photos = photos, onPhotoClick = {}, onPhotoLongClick = {})
    }
}