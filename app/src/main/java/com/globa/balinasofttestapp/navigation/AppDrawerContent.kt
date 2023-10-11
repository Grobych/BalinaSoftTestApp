package com.globa.balinasofttestapp.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.globa.balinasofttestapp.R
import com.globa.balinasofttestapp.common.ui.theme.BalinaSoftTestAppTheme
import com.globa.balinasofttestapp.common.ui.theme.Paddings
import kotlinx.coroutines.launch

@Composable
fun AppDrawerContent(
    drawerState: DrawerState,
    userName: String,
    defaultPick: NavItem,
    onClick: (NavItem) -> Unit
) {
    var currentPick by remember { mutableStateOf(defaultPick) }
    val coroutineScope = rememberCoroutineScope()


    val navigate = fun(navItem: NavItem){
        if (currentPick == navItem) {
            coroutineScope.launch {
                drawerState.close()
            }
            return
        }

        currentPick = navItem

        coroutineScope.launch {
            drawerState.close()
        }
        onClick(navItem)
    }

    ModalDrawerSheet {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = userName,
                    modifier = Modifier.padding(Paddings.small)
                )
                Divider()
                AppDrawerItem(
                    navItem = NavItem.PhotoListScreen,
                    iconId = R.drawable.ic_account_circle,
                    descriptionId = R.string.photos_menu_item,
                    nameId = R.string.photos_menu_item,
                    onClick = navigate
                )
                AppDrawerItem(
                    navItem = NavItem.MapScreen,
                    iconId = R.drawable.ic_map,
                    descriptionId = R.string.map_menu_item,
                    nameId = R.string.map_menu_item,
                    onClick = navigate
                )
            }
        }
    }
}

@Preview
@Composable
fun AppDrawerContentPreview() {
    BalinaSoftTestAppTheme {
        AppDrawerContent(
            drawerState = DrawerState(initialValue = DrawerValue.Open),
            userName = "User Name",
            defaultPick = NavItem.PhotoListScreen,
            onClick = {}
        )
    }
}
