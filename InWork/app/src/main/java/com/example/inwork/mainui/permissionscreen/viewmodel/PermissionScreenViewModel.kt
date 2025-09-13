package com.example.inwork.mainui.permissionscreen.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for handling the logic of the permission screen.
 */
class PermissionViewModel : ViewModel() {

    // A shared flow to emit navigation events to the UI.
    // SharedFlow is used here for one-time events.
    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    /**
     * Handles the result of the foreground location permission request.
     *
     * @param permissions A map of requested permissions to their granted status.
     * @return `true` if all permissions were granted, `false` otherwise.
     */
    fun onForegroundPermissionResult(permissions: Map<String, Boolean>): Boolean {
        val allPermissionsGranted = permissions.values.all { it }
        if (!allPermissionsGranted) {
            // If any permission is denied, trigger navigation to the login screen.
            navigateToLogin()
        }
        return allPermissionsGranted
    }

    /**
     * Called when the user makes a decision on the background location permission.
     * This function will always navigate to the login screen.
     */
    fun onBackgroundPermissionResult() {
        navigateToLogin()
    }

    /**
     * Called when the user explicitly denies the permission from the UI (e.g., clicks a "DENY" button).
     */
    fun onPermissionDenied() {
        navigateToLogin()
    }

    /**
     * Emits a navigation event to the login screen.
     * This is launched in the viewModelScope to ensure it's lifecycle-aware.
     */
    private fun navigateToLogin() {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.NavigateToLogin)
        }
    }
}

/**
 * A sealed interface to represent navigation events from the ViewModel.
 */
sealed interface NavigationEvent {
    /**
     * An event to navigate to the login screen.
     */
    data object NavigateToLogin : NavigationEvent
}