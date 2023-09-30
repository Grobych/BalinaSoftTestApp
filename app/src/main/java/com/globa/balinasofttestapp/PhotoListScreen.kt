package com.globa.balinasofttestapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoListScreen(
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
            text = "Photo List Screen",
            modifier = Modifier.padding(it)
        )
    }
}