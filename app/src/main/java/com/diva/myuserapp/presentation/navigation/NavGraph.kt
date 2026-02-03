package com.diva.myuserapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.diva.myuserapp.presentation.auth.forgot_password.ForgotPasswordScreen
import com.diva.myuserapp.presentation.auth.login.LoginScreen
import com.diva.myuserapp.presentation.auth.register.RegisterScreen
import com.diva.myuserapp.presentation.home.HomeScreen
import com.diva.myuserapp.utils.Constants

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Constants.ROUTE_LOGIN) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Constants.ROUTE_REGISTER) {
                        launchSingleTop = true
                    }
                },
                onNavigateToForgotPassword = {
                    navController.navigate(Constants.ROUTE_FORGOT_PASSWORD) {
                        launchSingleTop = true
                    }
                },
                onLoginSuccess = {
                    navController.navigate(Constants.ROUTE_HOME) {
                        popUpTo(Constants.ROUTE_LOGIN) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Constants.ROUTE_REGISTER) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.navigate(Constants.ROUTE_LOGIN) {
                        popUpTo(Constants.ROUTE_REGISTER) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Constants.ROUTE_FORGOT_PASSWORD) {
            ForgotPasswordScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Constants.ROUTE_HOME) {
            HomeScreen(
                onLogout = {
                    navController.navigate(Constants.ROUTE_LOGIN) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}