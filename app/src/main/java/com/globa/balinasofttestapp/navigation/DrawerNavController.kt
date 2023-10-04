package com.globa.balinasofttestapp.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.globa.balinasofttestapp.MapScreen
import com.globa.balinasofttestapp.camera.SendPhotoScreen
import com.globa.balinasofttestapp.photos.PhotoListScreen
import com.globa.balinasofttestappphotodetails.PhotoDetailScreen

@Composable
fun DrawerNavController(
    navigateToLogin: () -> Unit
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val navigateToPhotoList: () -> Unit = {
        navController.navigate(NavRoutes.PhotoList.name)
    }
    val navigateToPhotoDetails: (Int) -> Unit = {
        navController.navigate(NavRoutes.PhotoDetails.name + "?photoId=$it")
    }
    val navigateToMap: () -> Unit = {
        navController.navigate(NavRoutes.Map.name)
    }
    val navigateToCamera: () -> Unit = {
        navController.navigate(NavRoutes.Camera.name)
    }
    val navigateToBack: () -> Unit = {
        navController.popBackStack()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawerContent(
                drawerState = drawerState,
                userName = "Tempo Name",
                defaultPick = NavItem.PhotoListScreen
            ) { onUserPickedOption ->
                when (onUserPickedOption) {
                    NavItem.PhotoListScreen -> {
                        navigateToPhotoList()
                    }
                    NavItem.MapScreen -> {
                        navigateToMap()
                    }
                }
            }
        }
    ) {
        NavHost(navController = navController, startDestination = NavRoutes.PhotoList.name) {
            composable(
                route = NavRoutes.PhotoList.name
            ) {
                PhotoListScreen(
                    drawerState = drawerState,
                    onPhotoClick = navigateToPhotoDetails,
                    navigateToCamera = navigateToCamera
                )
            }
            composable(
                route = NavRoutes.PhotoDetails.name +"?photoId={photoId}",
                arguments = listOf(navArgument("photoId") { type = NavType.IntType })
            ) {
                PhotoDetailScreen(
                    onBackButtonClick = navigateToBack
                )
            }
            composable(
                route = NavRoutes.Camera.name
            ) {
                SendPhotoScreen(
                    onBackButtonClick = navigateToBack
                )
            }
            composable(
                route = NavRoutes.Map.name
            ) {
                MapScreen(drawerState = drawerState)
            }
        }
    }
}
enum class NavRoutes {
    PhotoList,
    PhotoDetails,
    Camera,
    Map
}

enum class NavItem {
    PhotoListScreen, MapScreen
}