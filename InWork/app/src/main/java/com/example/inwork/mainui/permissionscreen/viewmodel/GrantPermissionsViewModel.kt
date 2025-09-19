package com.example.inwork.mainui.permissionscreen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel

// Sealed interface to represent user actions on the permission screen
sealed class PermissionEvent {
    data object GrantLocationClicked : PermissionEvent()
    data object GrantBackgroundLocationClicked : PermissionEvent()
    data object GrantScreenTimeClicked : PermissionEvent()
    data object GrantPhoneClicked : PermissionEvent()
    data object GrantNotificationsClicked : PermissionEvent()
}

class GrantPermissionsViewModel : ViewModel() {

    fun onEvent(event: PermissionEvent) {
        when (event) {
            PermissionEvent.GrantLocationClicked -> requestLocationPermission()
            PermissionEvent.GrantBackgroundLocationClicked -> requestBackgroundLocationPermission()
            PermissionEvent.GrantScreenTimeClicked -> requestScreenTimePermission()
            PermissionEvent.GrantPhoneClicked -> requestPhonePermission()
            PermissionEvent.GrantNotificationsClicked -> requestNotificationsPermission()
        }
    }

    private fun requestLocationPermission() {
        // TODO: Add your logic to request the Location permission
        Log.d("Permissions", "Location permission request triggered.")
    }

    private fun requestBackgroundLocationPermission() {
        // TODO: Add your logic to request Background Location
        Log.d("Permissions", "Background Location permission request triggered.")
    }

    private fun requestScreenTimePermission() {
        // TODO: Add your logic to request Screen Time permission
        Log.d("Permissions", "Screen Time permission request triggered.")
    }

    private fun requestPhonePermission() {
        // TODO: Add your logic to request Phone permission
        Log.d("Permissions", "Phone permission request triggered.")
    }

    private fun requestNotificationsPermission() {
        // TODO: Add your logic to request Notifications permission
        Log.d("Permissions", "Notifications permission request triggered.")
    }
}