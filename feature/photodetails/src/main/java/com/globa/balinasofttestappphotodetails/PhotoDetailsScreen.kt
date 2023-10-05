package com.globa.balinasofttestappphotodetails

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.globa.balinasofttestapp.comments.api.model.Comment
import com.globa.balinasofttestapp.common.ui.composable.BackHeader
import com.globa.balinasofttestapp.common.ui.composable.LoadingAnimation
import com.globa.balinasofttestapp.common.ui.theme.BalinaSoftTestAppTheme
import com.globa.balinasofttestapp.common.util.DateFormatter
import com.globa.balinasofttestapp.photos.api.model.PhotoDetails

@Composable
fun PhotoDetailScreen(
    onBackButtonClick: () -> Unit,
    viewModel: PhotoDetailsViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()
    val commentsUiState = viewModel.commentsUiState.collectAsState()
    val commentTextField = viewModel.commentTextFieldState.collectAsState()

    val sendComment = fun() {
        viewModel.addComment()
    }
    val onCommentFieldChange = fun(text: String) {
        viewModel.onCommentTextFieldChange(text)
    }

    if (commentsUiState.value is CommentsUiState.Done) {
        if ((commentsUiState.value as CommentsUiState.Done).showDeleteDialog)
            AlertDialog(
                text = { Text(text = "Delete comment?")},
                dismissButton = { Button(onClick = { viewModel.onRequestDenied() }) {
                    Text(text = "Cancel")
                } },
                onDismissRequest = { viewModel.onRequestDenied() },
                confirmButton = { Button(onClick = { viewModel.onRequestApproved() }) {
                    Text(text = "Ok")
                } }
            )
    }

    val requestToRemove = fun(id: Int) {
        viewModel.requestToRemoveComment(id)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            BackHeader(onBackButtonClick = onBackButtonClick)
        },
        bottomBar = {
            AddCommentField(
                text = commentTextField.value,
                onSendButtonClick =  sendComment,
                onTextFieldChanged =  onCommentFieldChange
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(it)
        ) {
            when (val state = uiState.value) {
                is PhotoDetailsUiState.Done -> DonePhotoDetailsScreen(photo = state.photo)
                is PhotoDetailsUiState.Error -> ErrorPhotoDetailsScreen(errorMessage = state.message)
                PhotoDetailsUiState.Loading -> LoadingAnimation(modifier = Modifier.fillMaxWidth())
            }
            when (val state = commentsUiState.value) {
                is CommentsUiState.Done -> {
                    val comments = state.comments.collectAsLazyPagingItems()
                    CommentsScreen(
                        comments = comments,
                        onCommentLongClick = requestToRemove
                    )
                }
                is CommentsUiState.Error -> ErrorPhotoDetailsScreen(errorMessage = state.message)
                CommentsUiState.Loading -> LoadingAnimation(modifier = Modifier.fillMaxWidth())
            }
        }
    }





}

@Composable
fun DonePhotoDetailsScreen(
    modifier: Modifier = Modifier,
    photo: PhotoDetails
) {
    Column(
        modifier = modifier.fillMaxWidth(),
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
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CommentsScreen(
    modifier: Modifier = Modifier,
    comments: LazyPagingItems<Comment>,
    onCommentLongClick: (Int) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        LazyColumn(
            modifier = modifier
        ) {
            items(comments.itemCount) { index ->
                val comment = comments[index]
                comment?.let {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .background(color = MaterialTheme.colorScheme.primaryContainer)
                            .combinedClickable(
                                onClick = {},
                                onLongClick = { onCommentLongClick(comment.id)}
                            )
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.CenterStart),
                            text = it.text
                        )
                        Text(
                            modifier = Modifier.align(Alignment.BottomEnd),
                            text = DateFormatter.getExtendDate(it.date)
                        )
                    }
                }
            }
            when (comments.loadState.refresh) {
                is LoadState.Loading -> item {
                    LoadingAnimation(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    )
                }

                else -> {}
            }
            when (comments.loadState.append) {
                is LoadState.Loading -> item {
                    LoadingAnimation(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    )
                }

                else -> {}
            }
        }
    }


}

@Composable
fun AddCommentField(
    modifier: Modifier = Modifier,
    text: String,
    onSendButtonClick: () -> Unit,
    onTextFieldChanged: (String) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        OutlinedTextField(
            modifier = Modifier.requiredWidth(320.dp),
            value = text,
            onValueChange = {onTextFieldChanged(it)},
            placeholder = { Text(text = "Add comment")},
            singleLine = true
        )
        IconButton(
            enabled = text.isNotEmpty(),
            onClick = { onSendButtonClick() }
        ) {
            Icon(
                modifier = Modifier.size(30.dp),
                painter = painterResource(
                    id = com.globa.balinasofttestapp.common.R.drawable.ic_send),
                contentDescription = "Send")
        }
    }
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
            text = errorMessage,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun AddCommentFieldPreview() {
    BalinaSoftTestAppTheme {
        Surface(
            modifier = Modifier.width(360.dp)
        ) {
            AddCommentField(text = "test comment...", onSendButtonClick = {}, onTextFieldChanged = {})
        }
    }
}
