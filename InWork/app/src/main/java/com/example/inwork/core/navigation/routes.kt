package com.example.inwork.core.navigation

/**
 * A sealed class to define the navigation routes in a type-safe way.
 *
 * @property route The string identifier for the route.
 */
sealed class Screen(val route: String) {
    /**
     * Represents the Splash screen.
     */
    data object Splash : Screen("Splash")

    /**
     * Represents the Onboarding screen.
     */
    data object Onboarding : Screen("Onboarding")

    /**
     * Represents the Permission screen.
     */
    data object Permission : Screen("Permission")

    data object Login : Screen("Login")

    data object Signup : Screen("Signup")

    data object adminHome : Screen("adminHome")

    data object AllMenu : Screen("all_menu")

}