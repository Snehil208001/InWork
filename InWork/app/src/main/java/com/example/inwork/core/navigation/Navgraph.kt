package com.example.inwork.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.inwork.mainui.authenticationscreen.ui.LoginScreen
import com.example.inwork.mainui.authenticationscreen.ui.SignUpScreen
import com.example.inwork.mainui.permissionscreen.ui.PermissionScreen
import com.example.inwork.mainui.splashscreen.ui.OnboardingScreen
import com.example.inwork.mainui.splashscreen.ui.SplashScreen

@Composable
fun MyAppNav(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route, // Use Screen sealed class
        modifier = modifier
    ) {
        composable(Screen.Splash.route) { // Use Screen.Splash.route
            SplashScreen(navController = navController)
        }
        composable(Screen.Onboarding.route) { // Use Screen.Onboarding.route
            OnboardingScreen(navController = navController)
        }
        composable(Screen.Permission.route) { // Use Screen.Permission.route
            PermissionScreen(navController = navController)
        }
        composable(Screen.Login.route) { // Use Screen.Login.route
            LoginScreen(navController = navController)
        }
        composable(Screen.Signup.route) { // Use Screen.Signup.route
            SignUpScreen(navController = navController)
        }

    }
}