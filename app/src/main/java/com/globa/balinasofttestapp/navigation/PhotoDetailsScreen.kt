package com.globa.balinasofttestapp.navigation

import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoDetailsScreen(
    drawerState: DrawerState
) {
    Surface {
        Text(text = "Photo Details")
    }
}