package com.example.inwork.core.navigation

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.inwork.mainui.addemployeescreen.ui.AddEmployeeScreen
import com.example.inwork.mainui.addofficescreen.ui.AddOfficeScreen
import com.example.inwork.mainui.addofficescreen.ui.OfficeLocationPickerScreen
import com.example.inwork.mainui.admineventscreen.ui.AddEventScreen
import com.example.inwork.mainui.adminhomescreen.ui.AdminHomeScreen
import com.example.inwork.mainui.adminhomescreen.ui.AllMenuContent
import com.example.inwork.mainui.adminsettings.ui.AdminSettingsScreen
import com.example.inwork.mainui.authenticationscreen.ui.LoginScreen
import com.example.inwork.mainui.authenticationscreen.ui.SignUpScreen
import com.example.inwork.mainui.contactusscreen.ui.ContactUsContent
import com.example.inwork.mainui.noticescreen.ui.SendNoticeScreen
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
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        // Core App Flow
        composable(Screen.Splash.route) { SplashScreen(navController = navController) }
        composable(Screen.Onboarding.route) { OnboardingScreen(navController = navController) }
        composable(Screen.Permission.route) { PermissionScreen(navController = navController) }
        composable(Screen.Login.route) { LoginScreen(navController = navController) }
        composable(Screen.Signup.route) { SignUpScreen(navController = navController) }
        composable(Screen.adminHome.route) { AdminHomeScreen(navController = navController) }
        composable(Screen.userHome.route) { UserHomeScreen(navController = navController) }
        composable(Screen.AllMenu.route) { AllMenuContent(navController = navController) }
        composable(Screen.OfficeLocationPicker.route) { OfficeLocationPickerScreen(navController = navController) }
        composable(Screen.GrantPermissionsScreen.route) { GrantPermissionsScreen() }
        composable(Screen.AdminSettingsScreen.route) { AdminSettingsScreen() }

        // Navigation to Existing Screens
        composable(Screen.AddOffice.route) {
            AddOfficeScreen(navController = navController)
        }
        composable(Screen.AddEmployee.route) {
            AddEmployeeScreen(onNavigateBack = { navController.navigateUp() })
        }
        composable(Screen.AddEvent.route) { AddEventScreen(navController = navController) }
        composable(Screen.ContactUs.route) { ContactUsContent() }

        // CORRECTED function call for SendNoticeScreen
        composable(Screen.SentNotices.route) {
            SendNoticeScreen(
                onSendNotice = { title, notice ->
                    // TODO: Implement your logic to send the notice here
                    // For now, we can just show a Toast message as a placeholder
                    Toast.makeText(context, "Notice Sent: $title", Toast.LENGTH_SHORT).show()
                    navController.navigateUp() // Navigate back after sending
                }
            )
        }

        // Placeholders for Future Screens
        composable(Screen.Profile.route) { Text(text = "Profile Screen") }
        composable(Screen.News.route) { Text(text = "News Screen") }
        composable(Screen.Weather.route) { Text(text = "Weather Screen") }
        composable(Screen.AllEmployees.route) { Text(text = "All Employees Screen") }
        composable(Screen.EmployeeStatus.route) { Text(text = "Employee Status Screen") }
        composable(Screen.LeaveRequest.route) { Text(text = "Leave Request Screen") }
        composable(Screen.MonthlyReports.route) { Text(text = "Monthly Reports Screen") }
        composable(Screen.AllOffices.route) { Text(text = "All Offices Screen") }
        composable(Screen.ScreenTime.route) { Text(text = "Screen Time Screen") }
    }
}