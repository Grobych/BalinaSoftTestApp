package com.globa.balinasofttestapp.permissions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissions(
    multiplePermissionState: MultiplePermissionsState,
    content: @Composable () -> Unit
) {
    if (multiplePermissionState.allPermissionsGranted) { //TODO: not all
        content()
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
