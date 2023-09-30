package com.globa.balinasofttestapp

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.globa.balinasofttestapp.ui.theme.BalinaSoftTestAppTheme
import com.globa.balinasofttestapp.ui.theme.headerHeight

@Composable
fun MenuHeader(
    modifier: Modifier = Modifier,
    onMenuButtonClick: () -> Unit
) {
    Row(
        modifier = modifier
            .height(headerHeight)
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        IconButton(
            onClick = { onMenuButtonClick() },
            modifier = Modifier
                .size(50.dp)
                .padding(start = 10.dp)
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_menu) , contentDescription = "Menu")
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MenuHeaderPreview() {
    BalinaSoftTestAppTheme {
        MenuHeader {}
    }
}

@Composable
fun EmptyHeader(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(headerHeight)
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ){

    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun EmptyHeaderPreview() {
    BalinaSoftTestAppTheme {
        EmptyHeader()
    }
}

@Composable
fun BackHeader(
    modifier: Modifier = Modifier,
    onBackButtonClick: () -> Unit
) {
    Row(
        modifier = modifier
            .height(headerHeight)
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        IconButton(
            onClick = { onBackButtonClick() },
            modifier = Modifier
                .size(50.dp)
                .padding(start = 10.dp)
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_back) , contentDescription = "Menu")
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BackHeaderPreview() {
    BalinaSoftTestAppTheme {
        BackHeader {}
    }
}