package com.example.inwork.mainui.adminhomescreen.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.example.inwork.mainui.adminhomescreen.ui.AdminScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Represents the state of the AdminHomeScreen.
 *
 * @property currentScreen The currently selected screen in the bottom navigation.
 * @property hasLocationPermission True if fine location permission is granted, false otherwise.
 */
data class AdminHomeState(
    val currentScreen: AdminScreen = AdminScreen.Home,
    val hasLocationPermission: Boolean = false
)

/**
 * Defines the events that can be sent from the UI to the ViewModel.
 */
sealed class AdminHomeEvent {
    /**
     * Event triggered when a new screen is selected from the bottom navigation bar.
     * @param screen The new screen to display.
     */
    data class ScreenSelected(val screen: AdminScreen) : AdminHomeEvent()

    /**
     * Event to update the location permission status.
     * @param isGranted True if the permission was granted, false otherwise.
     */
    data class LocationPermissionUpdated(val isGranted: Boolean) : AdminHomeEvent()

    /**
     * Event to check the current location permission status.
     * @param context The application context.
     */
    data class CheckLocationPermission(val context: Context) : AdminHomeEvent()
}


/**
 * ViewModel for the AdminHomeScreen.
 */
class AdminHomeViewModel : ViewModel() {

    private val _state = MutableStateFlow(AdminHomeState())
    val state: StateFlow<AdminHomeState> = _state.asStateFlow()

    /**
     * Handles events sent from the UI.
     * @param event The event to handle.
     */
    fun onEvent(event: AdminHomeEvent) {
        when (event) {
            is AdminHomeEvent.ScreenSelected -> {
                _state.update { it.copy(currentScreen = event.screen) }
            }
            is AdminHomeEvent.LocationPermissionUpdated -> {
                _state.update { it.copy(hasLocationPermission = event.isGranted) }
            }
            is AdminHomeEvent.CheckLocationPermission -> {
                val isGranted = ContextCompat.checkSelfPermission(
                    event.context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                _state.update { it.copy(hasLocationPermission = isGranted) }
            }
        }
    }
}