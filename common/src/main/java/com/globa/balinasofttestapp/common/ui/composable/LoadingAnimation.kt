package com.globa.balinasofttestapp.common.ui.composable

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.globa.balinasofttestapp.common.R
import com.globa.balinasofttestapp.common.ui.theme.BalinaSoftTestAppTheme

@Composable
fun LoadingAnimation(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "Rotating Animation")
    val degree = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(
                durationMillis = 1000,
                delayMillis = 0,
                easing = LinearEasing
            )
        ),
        label = "Rotating Animation"
    )
    Image(
        modifier = modifier.rotate(degree.value),
        imageVector = ImageVector.vectorResource(R.drawable.loading_img),
        contentDescription = stringResource(id = R.string.loading_string)
    )
}

@Preview
@Composable
fun LoadingAnimationPreview() {
    BalinaSoftTestAppTheme {
        Surface {
            LoadingAnimation()
        }
    }
}