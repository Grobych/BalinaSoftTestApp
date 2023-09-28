package com.globa.balinasofttestapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import com.globa.balinasofttestapp.location.api.LocationResponse
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen() {
    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
    LocationPermissions(multiplePermissionState = permissionsState)
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissions(
    multiplePermissionState: MultiplePermissionsState
) {
    if (multiplePermissionState.allPermissionsGranted) {
        ScreenAfterPermissions()
    } else {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Column {
                Text(text = "App requires permissions")
                Button(onClick = { multiplePermissionState.launchMultiplePermissionRequest() }) {
                    Text(text = "Grant permissions")
                }
            }
        }

    }
}

@Composable
fun ScreenAfterPermissions(
    viewModel: TestLocationViewModel = hiltViewModel()
) {
    val locationState = viewModel.locationState.collectAsState()
    viewModel.flushLocation()
    when (val state = locationState.value) {
        is LocationResponse.Unset -> {
            Text(text = "Unset")
        }
        is LocationResponse.Error -> {
            Text(text = state.message)
        }
        is LocationResponse.Success -> {
            Text(text = "${state.location}")
        }
    }
}