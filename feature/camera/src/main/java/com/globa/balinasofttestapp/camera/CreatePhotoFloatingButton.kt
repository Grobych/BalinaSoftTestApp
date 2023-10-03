package com.globa.balinasofttestapp.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.globa.balinasofttestapp.common.R
import com.globa.balinasofttestapp.common.ui.theme.BalinaSoftTestAppTheme
import com.globa.balinasofttestapp.common.util.DateFormatter
import java.io.File
import java.util.Date
import java.util.Objects

@Composable
fun CreatePhotoFloatingButton(
    modifier: Modifier = Modifier,
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

    FloatingActionButton(
        modifier = modifier.size(50.dp),
        shape = RoundedCornerShape(50.dp),
        onClick = {
            val permissionCheckResult =
                ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                cameraLauncher.launch(uri)
            } else {
                // Request a permission
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    ) {
        Icon(painter = painterResource(id = R.drawable.ic_add), contentDescription = "Add photo")
    }
}


@Preview
@Composable
fun CreatePhotoFloatingButtonPreview() {
    BalinaSoftTestAppTheme {
        Surface {
            CreatePhotoFloatingButton{}
        }
    }
}

fun Context.createImageFile(): File {
    val timeStamp = DateFormatter.getExtendDate(Date().time)
    val imageFileName = "JPEG_" + timeStamp + "_"
    return File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
}