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
    val userName = viewModel.userName.collectAsState(initial = "")

    val navigateToMain: () -> Unit = {
        navController.navigate(AppRoutes.Main.name)
    }
    val navigateToLogin: () -> Unit = {
        viewModel.logout()
        navController.navigate(AppRoutes.Login.name)
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
                userName = userName.value,
                onLogoutButtonClick = navigateToLogin
            )
        }
    }
}
enum class AppRoutes {
    Login,
    Main
}