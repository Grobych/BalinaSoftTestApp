package com.globa.balinasofttestapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.globa.balinasofttestapp.common.ui.composable.MenuHeader
import kotlinx.coroutines.launch

@Composable
fun MapScreen(
    drawerState: DrawerState
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            MenuHeader {
                coroutineScope.launch {
                    drawerState.open()
                }
            }
        }
    ) {
        Text(
            text = "Map Screen",
            modifier = Modifier.padding(it)
        )
    }
}