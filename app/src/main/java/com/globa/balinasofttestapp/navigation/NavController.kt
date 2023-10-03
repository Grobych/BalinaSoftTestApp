package com.globa.balinasofttestapp.navigation

import androidx.compose.material3.DrawerState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.globa.balinasofttestapp.MapScreen
import com.globa.balinasofttestapp.camera.SendPhotoScreen
import com.globa.balinasofttestapp.photos.PhotoListScreen
import com.globa.balinasofttestapp.login.AuthorizationScreen

fun NavGraphBuilder.mainGraph(
    drawerState: DrawerState,
    navController: NavController
) {
    navigation(startDestination = NavItem.PhotoListScreen.name, route = NavRoutes.MainRoute.name) {
        composable(NavItem.PhotoListScreen.name){
            val navigateToCamera = fun() {
                navController.navigate(NavRoutes.CameraRoute.name) {
                    popUpTo(NavItem.PhotoListScreen.name)
                }
            }
            val onPhotoClick = fun(id: Int) {}
            PhotoListScreen(
                drawerState = drawerState,
                onPhotoClick = onPhotoClick,
                navigateToCamera = navigateToCamera)
        }
        composable(NavItem.PhotoDetailsScreen.name){
            PhotoDetailsScreen(drawerState)
        }
        composable(NavItem.MapScreen.name){
            MapScreen(drawerState)
        }
    }
}

fun NavGraphBuilder.loginGraph(navController: NavController) {
    val navigateToMain = fun() {navController.navigate(NavItem.PhotoListScreen.name)}
    navigation(startDestination = NavItem.LoginScreen.name, route = NavRoutes.LoginRoute.name) {
        composable(NavItem.LoginScreen.name){
            AuthorizationScreen(navigateToMainScreen = navigateToMain)
        }
    }
}

fun NavGraphBuilder.cameraGraph(navController: NavController) {
    val navigateToBack = fun() {navController.popBackStack()}
    navigation(startDestination = NavItem.CameraScreen.name, route = NavRoutes.CameraRoute.name) {
        composable(NavItem.CameraScreen.name){
            SendPhotoScreen(onBackButtonClick = navigateToBack)
        }
    }
}

enum class NavRoutes {
    MainRoute,
    LoginRoute,
    CameraRoute
}

enum class NavItem {
    LoginScreen, PhotoListScreen, PhotoDetailsScreen, MapScreen, CameraScreen
}