package com.example.inwork.core.navigation

/**
 * A sealed class to define the navigation routes in a type-safe way.
 *
 * @property route The string identifier for the route.
 */
sealed class Screen(val route: String) {
    // Core App Flow
    data object Splash : Screen("Splash")
    data object Onboarding : Screen("Onboarding")
    data object Permission : Screen("Permission")
    data object Login : Screen("Login")
    data object Signup : Screen("Signup")
    data object adminHome : Screen("adminHome")
    data object userHome : Screen("userHome")
    data object AllMenu : Screen("all_menu")

    // Screens from AllMenu
    data object AddOffice : Screen("AddOffice")
    data object AdminSettingsScreen : Screen("admin_settings_screen")
    data object AddEmployee : Screen("add_employee")
    data object AddEvent : Screen("add_event")
    data object ContactUs : Screen("contact_us")
    data object SentNotices : Screen("sent_notices")

    // Placeholder routes for future screens
    data object Profile : Screen("profile")
    data object News : Screen("news")
    data object Weather : Screen("weather")
    data object AllEmployees : Screen("all_employees")
    data object EmployeeStatus : Screen("employee_status")
    data object LeaveRequest : Screen("leave_request")
    data object MonthlyReports : Screen("monthly_reports")
    data object AllOffices : Screen("all_offices")
    data object ScreenTime : Screen("screen_time")

    // Other existing screens
    data object OfficeLocationPicker : Screen("OfficeLocationPicker")
    data object GrantPermissionsScreen: Screen("grant_permissions_screen")
}