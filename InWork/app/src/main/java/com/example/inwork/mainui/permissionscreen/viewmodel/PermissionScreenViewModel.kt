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
            // If any permission is denied, trigger navigation to the onboarding screen.
            navigateToOnboarding()
        }
        return allPermissionsGranted
    }

    /**
     * Called when the user makes a decision on the background location permission.
     * This function will always navigate to the onboarding screen.
     */
    fun onBackgroundPermissionResult() {
        navigateToOnboarding()
    }

    /**
     * Called when the user explicitly denies the permission from the UI (e.g., clicks a "DENY" button).
     */
    fun onPermissionDenied() {
        navigateToOnboarding()
    }

    /**
     * Emits a navigation event to the onboarding screen.
     * This is launched in the viewModelScope to ensure it's lifecycle-aware.
     */
    private fun navigateToOnboarding() {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.NavigateToOnboarding)
        }
    }
}

/**
 * A sealed interface to represent navigation events from the ViewModel.
 */
sealed interface NavigationEvent {
    /**
     * An event to navigate to the onboarding screen.
     */
    data object NavigateToOnboarding : NavigationEvent
}