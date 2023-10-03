package com.globa.balinasofttestapp.camera

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.globa.balinasofttestapp.common.ui.composable.BackHeader
import com.globa.balinasofttestapp.common.ui.composable.LoadingAnimation
import com.globa.balinasofttestapp.common.ui.composable.permissions.LocationPermissions
import com.globa.balinasofttestapp.common.util.readUri
import java.util.Objects

@Composable
fun SendPhotoScreen(
    onBackButtonClick: () -> Unit
) {
    LocationPermissions {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            BackHeader(onBackButtonClick = onBackButtonClick)
            SendPhotoScreenContent {
                onBackButtonClick()
            }
        }
    }
}

@Composable
fun SendPhotoScreenContent(
    viewModel: SendPhotoViewModel = hiltViewModel(),
    onBackButtonClick: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsState()

    val context = LocalContext.current

    val onPhotoCaptured = fun(uri: Uri) {
        val bytes = readUri(context, uri)
        if (bytes != null) viewModel.onPhotoCaptured(bytes)
        else onBackButtonClick()
    }

    val onSendButtonClick = fun() {
        viewModel.onSendButtonClick()
    }

    when (val state = uiState.value) {
        SendPhotoUiState.Creating -> TakingPhoto(onPhotoCaptured = onPhotoCaptured)
        SendPhotoUiState.Done -> DoneScreen(onDoneButtonClick = onBackButtonClick)
        is SendPhotoUiState.Error -> ErrorScreen(
            errorMessage = state.message,
            onBackButtonClick = onBackButtonClick
        )
        is SendPhotoUiState.Processing -> ProcessingScreen(message = state.stateMessage)
        is SendPhotoUiState.ReadyToSend -> ReadyToSendScreen(
            bitmap = state.bitmap,
            location = state.location,
            onSendButtonClick = onSendButtonClick
        )
        SendPhotoUiState.Sending -> ProcessingScreen(message = "Sending photo...")
    }
}

@Composable
fun TakingPhoto(
    onPhotoCaptured: (Uri) -> Unit
) {
    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        "com.globa.balinasofttestapp.provider", file
    )

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            onPhotoCaptured(uri)
            Toast.makeText(context, uri.toString(), Toast.LENGTH_LONG).show()
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) cameraLauncher.launch(uri)
    }

    val permissionCheckResult =
        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
        SideEffect {
            cameraLauncher.launch(uri)
        }
    } else {
        // Request a permission
        SideEffect {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }
}

@Composable
fun DoneScreen(
    modifier: Modifier = Modifier,
    onDoneButtonClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Photo has been uploaded successfully!")
        Button(onClick = { onDoneButtonClick() }) {
            Text(text = "Return")
        }
    }
}

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    errorMessage: String,
    onBackButtonClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error
        )
        Button(onClick = { onBackButtonClick() }) {
            Text(text = "Return")
        }
    }
}

@Composable
fun ProcessingScreen(
    modifier: Modifier = Modifier,
    message: String
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error
        )
        LoadingAnimation(
            modifier = Modifier.size(200.dp)
        )
    }
}

@Composable
fun ReadyToSendScreen(
    modifier: Modifier = Modifier,
    bitmap: Bitmap,
    location: Location,
    onSendButtonClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(bitmap = bitmap.asImageBitmap(), contentDescription = "Photo to upload")
        Text(text = location.toString())
        Button(
            modifier = Modifier.padding(top = 50.dp),
            onClick = { onSendButtonClick() }
        ) {
            Text(text = "Send")
        }
    }
}