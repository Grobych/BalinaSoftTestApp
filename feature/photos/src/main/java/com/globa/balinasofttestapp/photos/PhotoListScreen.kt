package com.globa.balinasofttestapp.photos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.globa.balinasofttestapp.common.ui.composable.LoadingAnimation
import com.globa.balinasofttestapp.common.ui.composable.MenuHeader
import com.globa.balinasofttestapp.common.ui.theme.BalinaSoftTestAppTheme
import com.globa.balinasofttestapp.common.util.DateFormatter
import com.globa.balinasofttestapp.photos.api.model.Photo
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
fun PhotoListScreen(
    drawerState: DrawerState,
    viewModel: PhotoListViewModel = hiltViewModel(),
    onPhotoClick: (Int) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val uiState = viewModel.photosUiState.collectAsState()

    Scaffold(
        topBar = {
            MenuHeader {
                coroutineScope.launch {
                    drawerState.open()
                }
            }
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
                    onPhotoClick = onPhotoClick
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
            modifier = Modifier.padding(bottom = 10.dp),
            text = errorMessage
        )
    }
}

@Composable
private fun DonePhotoListScreen(
    modifier: Modifier = Modifier,
    photos: LazyPagingItems<Photo>,
    onPhotoClick: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier
    ) {
        items(photos.itemCount) { index ->
            val photo = photos[index]
            photo?.let {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    AsyncImage(
                        modifier = Modifier
                            .size(100.dp)
                            .clickable {
                                onPhotoClick(it.id)
                            },
                        model = it.url,
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
                        .height(100.dp))
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
                        .height(100.dp))
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
        DonePhotoListScreen(photos = photos, onPhotoClick = {})
    }
}