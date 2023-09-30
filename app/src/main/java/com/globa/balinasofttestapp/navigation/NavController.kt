package com.globa.balinasofttestapp.navigation

import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.globa.balinasofttestapp.MapScreen
import com.globa.balinasofttestapp.PhotoListScreen

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.mainGraph(drawerState: DrawerState) {
    navigation(startDestination = NavItem.PhotoListScreen.name, route = NavRoutes.MainRoute.name) {
        composable(NavItem.PhotoListScreen.name){
            PhotoListScreen(drawerState)
        }
        composable(NavItem.PhotoDetailsScreen.name){
            PhotoDetailsScreen(drawerState)
        }
        composable(NavItem.MapScreen.name){
            MapScreen(drawerState)
        }
        composable(NavItem.LoginScreen.name){
            //TODO: login screen
        }
    }
}

enum class NavRoutes {
    MainRoute,
    LoginRoute
}

enum class NavItem {
    LoginScreen, PhotoListScreen, PhotoDetailsScreen, MapScreen
}