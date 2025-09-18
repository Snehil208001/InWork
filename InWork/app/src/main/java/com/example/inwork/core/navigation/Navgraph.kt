package com.example.inwork.core.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.inwork.mainui.addofficescreen.ui.AddOfficeScreen
import com.example.inwork.mainui.addofficescreen.ui.OfficeLocationPickerScreen
import com.example.inwork.mainui.adminhomescreen.ui.AdminHomeScreen
import com.example.inwork.mainui.adminhomescreen.ui.AllMenuContent
import com.example.inwork.mainui.authenticationscreen.ui.LoginScreen
import com.example.inwork.mainui.authenticationscreen.ui.SignUpScreen
import com.example.inwork.mainui.permissionscreen.ui.GrantPermissionsScreen
import com.example.inwork.mainui.permissionscreen.ui.PermissionScreen
import com.example.inwork.mainui.splashscreen.ui.OnboardingScreen
import com.example.inwork.mainui.splashscreen.ui.SplashScreen
import com.example.inwork.mainui.userhomescreen.ui.UserHomeScreen

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
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
        composable(Screen.adminHome.route) { // Use Screen.adminHome.route
            AdminHomeScreen(navController = navController)
        }
        composable(Screen.userHome.route) {
            UserHomeScreen(navController = navController)
        }
        composable(Screen.AllMenu.route) { // Use Screen.AllMenu.route
            AllMenuContent(navController = navController)
        }
        composable(Screen.OfficeLocationPicker.route) {
            OfficeLocationPickerScreen(navController = navController)
        }
        composable(Screen.AddOffice.route) {
            AddOfficeScreen(navController = navController)
        }
        composable(route = Screen.GrantPermissionsScreen.route) {
            GrantPermissionsScreen(

                onGrantLocation = {
                    // Logic to request LOCATION permission
                },
                onGrantBackgroundLocation = {
                    // Logic to request BACKGROUND LOCATION permission
                },
                onGrantScreentime = {
                    // Logic to request SCREENTIME permission
                },
                onGrantPhone = {
                    // Logic to request PHONE permission
                },
                onGrantNotifications = {
                    // Logic to request NOTIFICATIONS permission
                }
            )
        }
    }

}
