package com.globa.balinasofttestapp.navigation

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.globa.balinasofttestapp.R
import com.globa.balinasofttestapp.ui.theme.BalinaSoftTestAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawerItem(
    modifier: Modifier = Modifier,
    iconId: Int,
    descriptionId: Int,
    nameId: Int,
    onClick: () -> Unit
) =
    Surface(
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = modifier
            .fillMaxWidth(),
        onClick = { onClick() },
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
        ) {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = stringResource(id = descriptionId),
                modifier = Modifier
                    .size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(id = nameId),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
            )
        }
    }

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun DrawerItemPreview() {
    BalinaSoftTestAppTheme {
        Column {
            AppDrawerItem(
                iconId = R.drawable.ic_account_circle,
                descriptionId = R.string.photos_menu_item,
                nameId = R.string.photos_menu_item
            ) {}

            Divider()

            AppDrawerItem(
                iconId = R.drawable.ic_map,
                descriptionId = R.string.map_menu_item,
                nameId = R.string.map_menu_item
            ) {}
        }

    }
}