package com.globa.balinasofttestapp.camera

import android.content.Context
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.globa.balinasofttestapp.common.R
import com.globa.balinasofttestapp.common.ui.theme.BalinaSoftTestAppTheme
import com.globa.balinasofttestapp.common.util.DateFormatter
import java.io.File
import java.util.Date

@Composable
fun CreatePhotoFloatingButton(
    modifier: Modifier = Modifier,
    navigateToCameraScreen: () -> Unit
) {

    FloatingActionButton(
        modifier = modifier.size(50.dp),
        shape = RoundedCornerShape(50.dp),
        onClick = {
            navigateToCameraScreen()
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