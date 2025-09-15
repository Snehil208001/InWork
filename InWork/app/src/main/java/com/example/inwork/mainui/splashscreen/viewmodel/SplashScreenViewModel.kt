package com.example.inwork.mainui.splashscreen.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inwork.core.navigation.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 * Represents the one-time events that can be triggered from the SplashScreenViewModel.
 */
sealed class SplashEvent {
    /**
     * An event to navigate to a specific destination.
     * @property route The destination route.
     */
    data class Navigate(val route: String) : SplashEvent()
}

/**
 * ViewModel for the SplashScreen.
 */
class SplashScreenViewModel : ViewModel() {

    private val _eventFlow = MutableSharedFlow<SplashEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    /**
     * Initiates the logic to decide the next screen after the splash animation.
     * @param context The application context, used for checking permissions.
     */
    fun decideNextScreen(context: Context) {
        viewModelScope.launch {
            // Wait for a duration that matches the splash screen animation
            delay(2500)

            val hasLocationPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            val destination = if (hasLocationPermission) {
                Screen.Login.route
            } else {
                Screen.Permission.route
            }

            _eventFlow.emit(SplashEvent.Navigate(destination))
        }
    }
}