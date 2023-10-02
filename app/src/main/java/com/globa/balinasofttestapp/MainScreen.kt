package com.globa.balinasofttestapp

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.globa.balinasofttestapp.navigation.AppDrawerContent
import com.globa.balinasofttestapp.navigation.NavItem
import com.globa.balinasofttestapp.navigation.NavRoutes
import com.globa.balinasofttestapp.navigation.loginGraph
import com.globa.balinasofttestapp.navigation.mainGraph
import com.globa.balinasofttestapp.common.ui.theme.BalinaSoftTestAppTheme

@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    viewModel: NavigationViewModel = hiltViewModel()
) {
    BalinaSoftTestAppTheme {
        Surface {
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
                                navController.navigate(onUserPickedOption.name) {
                                    popUpTo(NavRoutes.MainRoute.name)
                                }
                            }
                            NavItem.MapScreen -> {
                                navController.navigate(onUserPickedOption.name) {
                                    popUpTo(NavRoutes.MainRoute.name)
                                }
                            }
                            else -> {}
                        }
                    }
                }
            ) {
                val isLogin = viewModel.isAuthorized.collectAsState(initial = true) //TODO: rewrite with suspend/loading anim
                NavHost(
                    navController,
                    startDestination = if (isLogin.value) NavRoutes.MainRoute.name else NavRoutes.LoginRoute.name
                ) {
                    mainGraph(drawerState)
                    loginGraph(navController)
                }
            }
        }
    }
}

