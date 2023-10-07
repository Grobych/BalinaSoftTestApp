package com.globa.balinasofttestapp.camera

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

@Composable
fun CreatePhotoFloatingButton(
    modifier: Modifier = Modifier,
    navigateToCameraScreen: () -> Unit
) {

    FloatingActionButton(
        modifier = modifier.size(60.dp),
        shape = RoundedCornerShape(60.dp),
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