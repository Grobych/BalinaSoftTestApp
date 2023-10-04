package com.globa.balinasofttestapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.globa.balinasofttestapp.NavigationViewModel
import com.globa.balinasofttestapp.login.AuthorizationScreen

@Composable
fun AppNavController(
    viewModel: NavigationViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    val navigateToLogin: () -> Unit = {
        navController.navigate(AppRoutes.Login.name)
    }
    val navigateToMain: () -> Unit = {
        navController.navigate(AppRoutes.Main.name)
    }
    val isLogin = viewModel.isAuthorized.collectAsState(initial = false) //TODO: rewrite with suspend/loading anim
    NavHost(navController = navController, startDestination = if (!isLogin.value) AppRoutes.Login.name else AppRoutes.Main.name) {
        composable(
            route = AppRoutes.Login.name
        ) {
            AuthorizationScreen(
                navigateToMainScreen = navigateToMain
            )
        }
        composable(
            route = AppRoutes.Main.name
        ) {
            DrawerNavController(
                navigateToLogin = navigateToLogin
            )
        }
    }
}
enum class AppRoutes {
    Login,
    Main
}