package com.example.inwork.mainui.permissionscreen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel

// Sealed interface for user events on the Admin Settings screen
sealed class AdminSettingsEvent {
    data object GrantLocationClicked : AdminSettingsEvent()
    data object GrantStorageClicked : AdminSettingsEvent()
    data object GrantNotificationsClicked : AdminSettingsEvent()
}

class AdminSettingsViewModel : ViewModel() {

    fun onEvent(event: AdminSettingsEvent) {
        when (event) {
            is AdminSettingsEvent.GrantLocationClicked -> requestLocationPermission()
            is AdminSettingsEvent.GrantStorageClicked -> requestStoragePermission()
            is AdminSettingsEvent.GrantNotificationsClicked -> requestNotificationsPermission()
        }
    }

    private fun requestLocationPermission() {
        // TODO: Implement your actual logic to request location permission
        Log.d("AdminSettings", "Location permission request triggered.")
    }

    private fun requestStoragePermission() {
        // TODO: Implement your actual logic to request storage permission
        Log.d("AdminSettings", "Storage permission request triggered.")
    }

    private fun requestNotificationsPermission() {
        // TODO: Implement your actual logic to request notifications permission
        Log.d("AdminSettings", "Notifications permission request triggered.")
    }
}